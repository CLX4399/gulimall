package com.clx4399.gulimall.order.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: wei-xhh
 * @create: 2020-07-28
 */
@Data
public class WareSkuLockVo {
    private String orderSn; //订单号
    private List<OrderItemVo> locks; //需要锁住的所有库存信息
}
