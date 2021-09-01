package com.clx4399.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.order.entity.OmsOrderEntity;
import com.clx4399.gulimall.order.vo.OrderConfirmVo;
import com.clx4399.gulimall.order.vo.OrderSubmitVo;
import com.clx4399.gulimall.order.vo.SubmitOrderResponseVo;

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
}

