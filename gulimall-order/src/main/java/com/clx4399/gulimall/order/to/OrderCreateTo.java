package com.clx4399.gulimall.order.to;

import com.clx4399.gulimall.order.entity.OmsOrderEntity;
import com.clx4399.gulimall.order.entity.OmsOrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description:
 * @author: wei-xhh
 * @create: 2020-07-27
 */
@Data
public class OrderCreateTo {
    private OmsOrderEntity order;
    private List<OmsOrderItemEntity> orderItems;
    //订单计算的应付价格
    private BigDecimal payPrice;
    //运费
    private BigDecimal fare;
}
