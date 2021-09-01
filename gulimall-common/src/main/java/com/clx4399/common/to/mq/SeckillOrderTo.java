package com.clx4399.common.to.mq;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: wei-xhh
 * @create: 2020-08-02
 */
@Data
public class SeckillOrderTo {
    private String orderSn; //订单号
    private Long promotionSessionId; //活动场次id
    private Long skuId; //商品id
    private BigDecimal seckillPrice; //秒杀价格
    private Integer num; //购买数量
    private Long memberId; //会员id
}
