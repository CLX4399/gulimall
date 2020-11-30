package com.clx4399.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2020-11-28 15:34:48
 */
@Data
public class SpuBoundTo {

    /**
     *
     */
    private Long spuId;
    /**
     * 成长积分
     */
    private BigDecimal growBounds;
    /**
     * 购物积分
     */
    private BigDecimal buyBounds;
}
