package com.clx4399.seckill.services.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.clx4399.common.to.mq.SeckillOrderTo;
import com.clx4399.common.utils.R;
import com.clx4399.common.vo.MemberResponseVo;
import com.clx4399.seckill.feign.CouponFeignService;
import com.clx4399.seckill.feign.ProductFeignService;
import com.clx4399.seckill.interceptor.LoginUserInterceptor;
import com.clx4399.seckill.services.SeckillService;
import com.clx4399.seckill.to.SeckillSkuRedisTo;
import com.clx4399.seckill.vo.SeckillSessionWithSkus;
import com.clx4399.seckill.vo.SkuInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-09-06 15:08:25
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    RedissonClient redissonClient;

    private final String SESSIONS_CACHE_PREFIX = "seckill:sessions:";

    private final String SKUKILL_CACHE_PREFIX = "seckill:skus";

    //加+商品随机码
    private final String SKU_STOCK_SEMAPHORE = "sckill:stock:";

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    CouponFeignService couponFeignService;

    @Override
    public void uploadSeckillSkuLatest3Days() {
        R latest3DaysSession = couponFeignService.getLatest3DaysSession();
        if (latest3DaysSession.getCode() == 0){
            List<SeckillSessionWithSkus> data = latest3DaysSession.getData(new TypeReference<List<SeckillSessionWithSkus>>() {
            });
            if (data != null){
                //将该秒杀秒杀活动的某阶段对应sku保存数据库
                saveSessionInfos(data);
                //将该商品的信息以skuid为key保存
                saveSessionSkuInfos(data);
            }
        }
    }

    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillSkus() {
        //1、确定当前时间属于哪个秒杀场次
        long time = System.currentTimeMillis();
        Set<String> keys = redisTemplate.keys(SESSIONS_CACHE_PREFIX + "*");
        for (String key : keys) {
            String replace = key.replace(SESSIONS_CACHE_PREFIX, "");
            String[] s = replace.split("_");
            long start = Long.parseLong(s[0]);
            Long end = Long.parseLong(s[1]);
            if (time >= start && time <= end) {
                List<String> range = redisTemplate.opsForList().range(key, -100, 100);
                BoundHashOperations<String, String, String> hashOperations = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
                List<String> list = hashOperations.multiGet(range);
                if (list!=null){
                    List<SeckillSkuRedisTo> collect = list.stream().map(item -> {
                        SeckillSkuRedisTo seckillSkuRedisTo = JSONObject.parseObject(item, SeckillSkuRedisTo.class);
                        return seckillSkuRedisTo;
                    }).collect(Collectors.toList());
                    return  collect;
                }
            }
        }
        return null;
    }

    @Override
    public SeckillSkuRedisTo getSkuSeckillInfo(Long skuId) {
        //1、找到所有需要参与秒杀的商品的key
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        Set<String> keys = hashOps.keys();
        if (keys != null){
            String regx = "\\d_" + skuId;
            for (String key : keys) {
                if(Pattern.matches(regx,key)){
                    String s = hashOps.get(key);
                    SeckillSkuRedisTo seckillSkuRedisTo = JSONObject.parseObject(s, SeckillSkuRedisTo.class);
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis>seckillSkuRedisTo.getEndTime() || currentTimeMillis < seckillSkuRedisTo.getStartTime() ){
                        seckillSkuRedisTo.setRandomCode(null);
                    }
                    return seckillSkuRedisTo;
                }
            }
        }
        return null;
    }

    @Override
    public String kill(String killId, String key, Integer num) {
        MemberResponseVo memberInfo = LoginUserInterceptor.loginUser.get();

        BoundHashOperations<String, String, String> boundHashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        String json = boundHashOps.get(killId);
        if (StringUtils.isNotBlank(json)){
            SeckillSkuRedisTo seckillSkuRedisTo = JSONObject.parseObject(json, SeckillSkuRedisTo.class);
            Long startTime = seckillSkuRedisTo.getStartTime();
            Long endTime = seckillSkuRedisTo.getEndTime();
            long currentTimeMillis = System.currentTimeMillis();
            long ttl = endTime - currentTimeMillis;
            if (currentTimeMillis>startTime && currentTimeMillis<endTime){
                String randomCode = seckillSkuRedisTo.getRandomCode();
                Integer seckillLimit = seckillSkuRedisTo.getSeckillLimit();
                String code = seckillSkuRedisTo.getPromotionSessionId() + "_" + seckillSkuRedisTo.getSkuId();
                if (randomCode.equals(key) && code.equals(killId)){
                    if (num <= seckillLimit){
                        //4、验证这个人是否已经购买过。幂等性；如果秒杀成功就去占位 userId_SessionId_skuId
                        String redisKey = memberInfo.getId() + "_" + code;
                        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(redisKey, String.valueOf(num),ttl, TimeUnit.MILLISECONDS);
                        //该用户未秒杀该商品，标识信号量
                        if (aBoolean){
                            //判断是否还存秒杀库存
                            RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
                            boolean b = semaphore.tryAcquire(num);
                            if (b){
                                SeckillOrderTo seckillOrderTo = new SeckillOrderTo();
                                String timeId = IdWorker.getTimeId();
                                seckillOrderTo.setOrderSn(timeId);
                                seckillOrderTo.setPromotionSessionId(seckillSkuRedisTo.getPromotionSessionId());
                                seckillOrderTo.setSkuId(seckillSkuRedisTo.getSkuId());
                                seckillOrderTo.setSeckillPrice(seckillSkuRedisTo.getSeckillPrice());
                                seckillOrderTo.setNum(num);
                                seckillOrderTo.setMemberId(memberInfo.getId());
                                rabbitTemplate.convertAndSend("order-event-exchange","order.seckill.order",seckillOrderTo);
                                return timeId;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void saveSessionSkuInfos(List<SeckillSessionWithSkus> data) {
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        data.stream().forEach(item->{
            item.getRelationSkus().forEach(seckillSkuVo -> {
                String token = UUID.randomUUID().toString();
                if (!hashOperations.hasKey(seckillSkuVo.getPromotionSessionId()+"_"+seckillSkuVo.getSkuId())){
                    SeckillSkuRedisTo seckillSkuRedisTo = new SeckillSkuRedisTo();

                    seckillSkuRedisTo.setRandomCode(token);

                    R info = productFeignService.info(seckillSkuVo.getSkuId());
                    if (info.getCode() == 0){
                        SkuInfoVo skuInfo = info.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                        });
                        seckillSkuRedisTo.setSkuInfoVo(skuInfo);
                    }

                    //2、sku的秒杀信息
                    BeanUtils.copyProperties(seckillSkuVo, seckillSkuRedisTo);

                    //3、设置商品的秒杀时间信息
                    seckillSkuRedisTo.setStartTime(item.getStartTime().getTime());
                    seckillSkuRedisTo.setEndTime(item.getEndTime().getTime());

                    String jsonString = JSON.toJSONString(seckillSkuRedisTo);
                    hashOperations.put(seckillSkuVo.getPromotionSessionId().toString() + "_" + seckillSkuVo.getSkuId().toString(), jsonString);

                    RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                    semaphore.trySetPermits(seckillSkuVo.getSeckillCount());
                }
            });
        });
    }

    private void saveSessionInfos(List<SeckillSessionWithSkus> data) {
        data.stream().forEach(item->{
            Long startTime = item.getStartTime().getTime();
            Long endTime = item.getEndTime().getTime();
            String key = SESSIONS_CACHE_PREFIX + startTime + "_" + endTime;

            Boolean hasKey = redisTemplate.hasKey(key);
            if (!hasKey){
                List<String> collect = item.getRelationSkus().stream().map(skuVo -> skuVo.getPromotionSessionId() + "_" + skuVo.getSkuId())
                        .collect(Collectors.toList());
                redisTemplate.opsForList().leftPushAll(key,collect);
            }
        });
    }

}
