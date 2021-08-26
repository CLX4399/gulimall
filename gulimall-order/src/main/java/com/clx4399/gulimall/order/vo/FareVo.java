package com.clx4399.gulimall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: wei-xhh
 * @create: 2020-07-27
 */
@Data
public class FareVo {
    private MemberAddressVo address;
    private BigDecimal fare;
}
