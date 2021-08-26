package com.clx4399.gulimall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * 封装订单提交的数据
 * @author: wei-xhh
 * @create: 2020-07-27
 */
@Data
public class OrderSubmitVo {
    //收货地址id
    private Long addrId;

    //支付方式
    private Integer payType;

    //无需提交需要购买的商品，去购物车再获取一遍
    //优惠，发票

    //防重令牌
    private String orderToken;

    //应付价格
    private BigDecimal payPrice;

    //订单备注
    private String note;

    //用户相关信息，直接去session取出登录的用户
}
