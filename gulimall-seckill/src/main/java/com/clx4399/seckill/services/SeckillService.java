package com.clx4399.seckill.services;

import com.clx4399.seckill.to.SeckillSkuRedisTo;

import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-09-06 15:06:55
 */
public interface SeckillService {

    /**
     * @param
     * @return void
     * @author CLX
     * @describe: 上架近三天的秒杀商品
     * @date 2021/9/6 15:07
     */
    void  uploadSeckillSkuLatest3Days();

    /**
     * @param
     * @return java.util.List<com.clx4399.seckill.to.SeckillSkuRedisTo>
     * @author CLX
     * @describe: 查询秒杀商品信息
     * @date 2021/9/6 21:28
     */
    List<SeckillSkuRedisTo> getCurrentSeckillSkus();

    /**
     * @param skuId
     * @return com.clx4399.seckill.to.SeckillSkuRedisTo
     * @author CLX
     * @describe: 根据skuid获取秒杀商品信息
     * @date 2021/9/7 10:14
     */
    SeckillSkuRedisTo getSkuSeckillInfo(Long skuId);

    /**
     * @param killId
	 * @param key
	 * @param num
     * @return java.lang.String
     * @author CLX
     * @describe: 商品秒杀抢购
     * @date 2021/9/7 16:03
     */
    String kill(String killId, String key, Integer num);
}
