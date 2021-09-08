package com.clx4399.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.to.mq.SeckillOrderTo;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.order.entity.OmsOrderEntity;
import com.clx4399.gulimall.order.vo.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-08-02 21:58:48
 */
public interface OmsOrderService extends IService<OmsOrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @param
     * @return com.clx4399.gulimall.order.vo.OrderConfirmVo
     * @author CLX
     * @describe: 返回购物车结算所需数据
     * @date 2021/8/23 15:34
     */
    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    /**
     * @param vo
     * @return com.clx4399.gulimall.order.vo.SubmitOrderResponseVo
     * @author CLX
     * @describe: 下单：去创建订单，验令牌，验价格，锁库存
     * @date 2021/8/25 17:45
     */
    SubmitOrderResponseVo submitOrder(OrderSubmitVo vo);

    /**
     * @param orderSn
     * @return com.clx4399.gulimall.order.entity.OmsOrderEntity
     * @author CLX
     * @describe: 获取订单信息根据订单编号
     * @date 2021/8/31 20:24
     */
    OmsOrderEntity getOrderByOrderSn(String orderSn);

    /**
     * @param entity
     * @return void
     * @author CLX
     * @describe:  关闭订单并取消库存
     * @date 2021/9/1 21:30
     */
    void closeOrder(OmsOrderEntity entity);

    /**
     * @param orderSn
     * @return com.clx4399.gulimall.order.vo.PayVo
     * @author CLX
     * @describe: 获取订单的支付信息
     * @date 2021/9/2 11:36
     */
    PayVo getOrderPay(String orderSn);

    /**
     * @param params
     * @return com.clx4399.common.utils.PageUtils
     * @author CLX
     * @describe: 获取订单信息
     * @date 2021/9/2 20:29
     */
    PageUtils listWithItem(Map<String, Object> params);

    /**
     * @param vo
     * @return java.lang.String
     * @author CLX
     * @describe: 处理支付结果
     * @date 2021/9/2 22:12
     */
    String handlePayResult(PayAsyncVo vo);

    /**
     * @param seckillOrder
     * @return void
     * @author CLX
     * @describe: 秒杀商品创建订单
     * @date 2021/9/7 16:45
     */
    void createSeckillOrder(SeckillOrderTo seckillOrder);
}

