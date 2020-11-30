package com.clx4399.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2020-11-28 15:46:02
 */
@Data
public class SkuRedution {

    private long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
