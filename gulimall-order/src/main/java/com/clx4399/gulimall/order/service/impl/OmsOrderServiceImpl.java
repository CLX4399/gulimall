package com.clx4399.gulimall.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.common.util.UuidUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.clx4399.common.exception.NoStockException;
import com.clx4399.common.utils.R;
import com.clx4399.common.vo.MemberResponseVo;
import com.clx4399.gulimall.order.entity.OmsOrderItemEntity;
import com.clx4399.gulimall.order.enume.OrderStatusEnum;
import com.clx4399.gulimall.order.feign.CartFeignService;
import com.clx4399.gulimall.order.feign.MemberFeignService;
import com.clx4399.gulimall.order.feign.ProductFeignService;
import com.clx4399.gulimall.order.feign.WareFeignService;
import com.clx4399.gulimall.order.intercept.LoginUserInterceptor;
import com.clx4399.gulimall.order.service.OmsOrderItemService;
import com.clx4399.gulimall.order.to.OrderCreateTo;
import com.clx4399.gulimall.order.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;

import com.clx4399.gulimall.order.dao.OmsOrderDao;
import com.clx4399.gulimall.order.entity.OmsOrderEntity;
import com.clx4399.gulimall.order.service.OmsOrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import static com.clx4399.gulimall.order.constant.OrderConstant.USER_ORDER_TOKEN_PREFIX;


@Service("omsOrderService")
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderDao, OmsOrderEntity> implements OmsOrderService {

    private ThreadLocal<OrderSubmitVo> submitVoThreadLocal =  new ThreadLocal<>();

    @Autowired
    MemberFeignService memberFeignService;

    @Autowired
    CartFeignService cartFeignService;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    OmsOrderItemService orderItemService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OmsOrderEntity> page = this.page(
                new Query<OmsOrderEntity>().getPage(params),
                new QueryWrapper<OmsOrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = new OrderConfirmVo();
        MemberResponseVo memberRespVo = LoginUserInterceptor.loginUser.get();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        CompletableFuture<Void> addresFuture = CompletableFuture.runAsync(() -> {
            //获取收货地址信息
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<MemberAddressVo> address =
                    memberFeignService.getAddress(memberRespVo.getId());
            confirmVo.setAddress(address);
        }, executor);

        CompletableFuture<Void> orderItemFuture = CompletableFuture.runAsync(() -> {
            //获取需要结算的商品信息
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<OrderItemVo> items =
                    cartFeignService.getCurrentUserCartItems();
            confirmVo.setItems(items);
        }, executor).thenRunAsync(()->{
            List<OrderItemVo> items = confirmVo.getItems();
            List<Long> collect = items.stream().map(item -> item.getSkuId()).collect(Collectors.toList());
            R skuHasStock = wareFeignService.getSkuHasStock(collect);
            List<SkuStockVo> data = skuHasStock.getData(new TypeReference<List<SkuStockVo>>() {
            });
            if (data!=null && data.size()>0){
                Map<Long, Boolean> collectMap = data.stream().collect(Collectors.toMap(SkuStockVo::getSkuId, SkuStockVo::getStock));
                confirmVo.setStocks(collectMap);
            }
        });

        //获取用户积分信息
        Integer integration = memberRespVo.getIntegration();
        confirmVo.setIntegration(integration);

        //4、其他数据自动计算

        //生成验证令牌
        String token = UuidUtils.generateUuid();
        confirmVo.setOrderToken(token);
        redisTemplate.opsForValue().set(USER_ORDER_TOKEN_PREFIX+memberRespVo.getId(),token,15, TimeUnit.MINUTES);

        CompletableFuture.allOf(orderItemFuture,addresFuture).get();
        return confirmVo;
    }

    @Transactional
    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo vo) {
        submitVoThreadLocal.set(vo);

        SubmitOrderResponseVo response = new SubmitOrderResponseVo();
        MemberResponseVo memberRespVo = LoginUserInterceptor.loginUser.get();
        response.setCode(0);

        //1、验证令牌【令牌的对比和删除必须保证原子性】
        //0令牌失败，1删除成功
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        String orderToken = vo.getOrderToken();

        //执行redis脚本，当返回为0时则代表该订单已被提交并执行。
        Long executeResult = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList(USER_ORDER_TOKEN_PREFIX + memberRespVo.getId()), orderToken);
        if (executeResult == 0){
            //订单提交失败
            response.setCode(1);
        }else {
            //令牌验证成功
            //1、创建订单，订单项等信息
            OrderCreateTo order = createOrder();
            //2、验价
            BigDecimal payAmount = order.getOrder().getPayAmount();
            BigDecimal payPrice = vo.getPayPrice();
            double abs = Math.abs(payAmount.subtract(payPrice).doubleValue());

            //金额差距小于0.01则认为金额正确
            if (abs < 0.01) {
                //保存订单
                saveOrder(order);

                //4、库存锁定,只要有异常回滚订单数据。
                // 订单号，所有订单项（skuId,skuName,num）
                WareSkuLockVo lockVo = new WareSkuLockVo();
                lockVo.setOrderSn(order.getOrder().getOrderSn());
                List<OrderItemVo> locks = order.getOrderItems().stream().map(item -> {
                    OrderItemVo itemVo = new OrderItemVo();
                    itemVo.setSkuId(item.getSkuId());
                    itemVo.setCount(item.getSkuQuantity());
                    itemVo.setTitle(item.getSkuName());
                    return itemVo;
                }).collect(Collectors.toList());
                lockVo.setLocks(locks);

                //TODO 远程锁库存
                //问题：库存成功了，但是网络原因超时了，订单回滚，库存不回滚
                //为了保证高并发，库存服务自己回滚，可以发消息个库存服务；
                //库存服务本身也可以自动解锁模式 使用消息队列
                R r = wareFeignService.orderLockStock(lockVo);
                if(r.getCode() == 0){
                    //锁成功了
                    response.setOrder(order.getOrder());
                    int a = 10 / 0;
                    //TODO 订单创建成功发送消息给MQ
                    //rabbitTemplate.convertAndSend("order-event-exchange","order.create.order",order.getOrder());
                    return response;
                } else {
                    //锁定失败
                    String msg = (String) r.get("msg");
                    throw new NoStockException(msg);
                }

            }else {
                //金额校验失败
                response.setCode(2);
            }
        }
        return response;
    }

    /**
     * @param order
     * @return void
     * @author CLX
     * @describe:  保存订单信息
     * @date 2021/8/26 11:37
     */
    private void saveOrder(OrderCreateTo order) {
        OmsOrderEntity order1 = order.getOrder();
        order1.setModifyTime(new Date());
        save(order1);
        List<OmsOrderItemEntity> orderItems = order.getOrderItems();
        for (OmsOrderItemEntity orderItem : orderItems) {
            orderItemService.save(orderItem);
        }

    }

    /**
     * @param
     * @return com.clx4399.gulimall.order.to.OrderCreateTo
     * @author CLX
     * @describe: 创建订单信息
     * @date 2021/8/25 20:26
     */
    private OrderCreateTo createOrder() {
        OrderCreateTo orderCreateTo = new OrderCreateTo();
        //1、生成订单号
        String orderSn = IdWorker.getTimeId();
        OmsOrderEntity orderEntity = buildOrder(orderSn);

        //2、获取到所有的订单项
        List<OmsOrderItemEntity> itemEntities = buildOrderItems(orderSn);

        //3、验价,计算价格相关
        computePrice(orderEntity, itemEntities);
        orderCreateTo.setOrder(orderEntity);
        orderCreateTo.setOrderItems(itemEntities);
        return orderCreateTo;
    }

    /**
     * @param orderEntity
	 * @param itemEntities
     * @return void
     * @author CLX
     * @describe: 金额积分优惠计算
     * @date 2021/8/26 10:03
     */
    private void computePrice(OmsOrderEntity orderEntity, List<OmsOrderItemEntity> itemEntities) {
        BigDecimal total = new BigDecimal("0.0");
        //订单的总额，叠加每个订单项的总额信息
        BigDecimal coupon = new BigDecimal("0.0");
        BigDecimal integration = new BigDecimal("0.0");
        BigDecimal promotion = new BigDecimal("0.0");
        BigDecimal gift = new BigDecimal("0.0");
        BigDecimal growth = new BigDecimal("0.0");
        for (OmsOrderItemEntity entity : itemEntities) {
            coupon = coupon.add(entity.getCouponAmount());
            integration = integration.add(entity.getIntegrationAmount());
            promotion = promotion.add(entity.getPromotionAmount());
            total = total.add(entity.getRealAmount());
            gift = gift.add(new BigDecimal(entity.getGiftIntegration().toString()));
            growth = growth.add(new BigDecimal(entity.getGiftGrowth().toString()));

        }
        //1、订单价格相关
        orderEntity.setTotalAmount(total);
        //应付总额
        orderEntity.setPayAmount(total.add(orderEntity.getFreightAmount()));
        orderEntity.setPromotionAmount(promotion);
        orderEntity.setIntegrationAmount(integration);
        orderEntity.setCouponAmount(coupon);

        //设置积分等信息
        orderEntity.setIntegration(gift.intValue());
        orderEntity.setGrowth(growth.intValue());
        //0:未删除
        orderEntity.setDeleteStatus(0);
    }

    /**
     * @param orderSn
     * @return java.util.List<com.clx4399.gulimall.order.entity.OmsOrderItemEntity>
     * @author CLX
     * @describe:  获取所有选中订单项
     * @date 2021/8/26 9:20
     */
    private List<OmsOrderItemEntity> buildOrderItems(String orderSn) {
        List<OrderItemVo> currentUserCartItems = cartFeignService.getCurrentUserCartItems();
        if (currentUserCartItems!=null && currentUserCartItems.size() > 0){
            List<OmsOrderItemEntity> collect = currentUserCartItems.stream().map(cartItem -> {
                OmsOrderItemEntity entity = buildOrderItem(cartItem);
                entity.setOrderSn(orderSn);
                return entity;
            }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    /**
     * @param cartItem
     * @return com.clx4399.gulimall.order.entity.OmsOrderItemEntity
     * @author CLX
     * @describe: 构建订单购物项
     * @date 2021/8/26 9:22
     */
    private OmsOrderItemEntity buildOrderItem(OrderItemVo cartItem) {
        OmsOrderItemEntity itemEntity = new OmsOrderItemEntity();
        Long skuId = cartItem.getSkuId();
        R spuInfoBySkuId = productFeignService.getSpuInfoBySkuId(skuId);
        SpuInfoVo data = spuInfoBySkuId.getData("skuInfo",new TypeReference<SpuInfoVo>() {
        });
        itemEntity.setSpuId(data.getId());
        itemEntity.setSpuBrand(data.getBrandId().toString());
        itemEntity.setSpuName(data.getSpuName());
        itemEntity.setCategoryId(data.getCatalogId());
        //3、商品的sku信息
        itemEntity.setSkuId(cartItem.getSkuId());
        itemEntity.setSkuName(cartItem.getTitle());
        itemEntity.setSkuPic(cartItem.getImage());
        itemEntity.setSkuPrice(cartItem.getPrice());
        String skuAttr = StringUtils.collectionToDelimitedString(cartItem.getSkuAttr(), ";");
        itemEntity.setSkuAttrsVals(skuAttr);
        itemEntity.setSkuQuantity(cartItem.getCount());
        //4、优惠信息（不做）
        //5、积分信息
        itemEntity.setGiftGrowth(cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount().toString())).intValue());
        itemEntity.setGiftIntegration(cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount().toString())).intValue());
        //6、订单项的价格信息
        itemEntity.setPromotionAmount(new BigDecimal("0"));
        itemEntity.setCouponAmount(new BigDecimal("0"));
        itemEntity.setIntegrationAmount(new BigDecimal("0"));
        //当前订单项的实际金额,总额-各种优惠
        BigDecimal origin = itemEntity.getSkuPrice().multiply(new BigDecimal(itemEntity.getSkuQuantity().toString()));
        BigDecimal subtract = origin.subtract(itemEntity.getCouponAmount())
                .subtract(itemEntity.getPromotionAmount())
                .subtract(itemEntity.getIntegrationAmount());

        itemEntity.setRealAmount(subtract);
        return itemEntity;
    }

    /**
     * @return
     * @author CLX
     * @describe: 构建订单信息
     * @date 2021/8/25 20:30
     */
    private OmsOrderEntity buildOrder(String orderSn) {
        MemberResponseVo memberRespVo = LoginUserInterceptor.loginUser.get();
        OmsOrderEntity entity = new OmsOrderEntity();
        entity.setOrderSn(orderSn);
        entity.setMemberId(memberRespVo.getId());

        //获取收货地址信息
        OrderSubmitVo orderSubmitVo = submitVoThreadLocal.get();
        R fare = wareFeignService.getFare(orderSubmitVo.getAddrId());
        FareVo fareResp = fare.getData(new TypeReference<FareVo>() {
        });

        //设置运费信息
        entity.setFreightAmount(fareResp.getFare());
        //设置收货人信息
        entity.setReceiverCity(fareResp.getAddress().getCity());
        entity.setReceiverDetailAddress(fareResp.getAddress().getDetailAddress());
        entity.setReceiverName(fareResp.getAddress().getName());
        entity.setReceiverPhone(fareResp.getAddress().getPhone());
        entity.setReceiverProvince(fareResp.getAddress().getProvince());
        entity.setReceiverRegion(fareResp.getAddress().getRegion());
        entity.setReceiverPostCode(fareResp.getAddress().getPostCode());
        //设置订单的状态信息
        entity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        entity.setAutoConfirmDay(7);
        return entity;
    }

}
