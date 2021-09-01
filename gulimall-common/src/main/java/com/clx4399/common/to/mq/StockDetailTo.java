package com.clx4399.common.to.mq;

import lombok.Data;

/**
 * @description:
 * @author: wei-xhh
 * @create: 2020-07-30
 */
@Data
public class StockDetailTo {
    private Long id;
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * sku_name
     */
    private String skuName;
    /**
     * 购买个数
     */
    private Integer skuNum;
    /**
     * 工作单id
     */
    private Long taskId;

    /**
     * 仓库id
     */
    private Long wareId;

    /**
     * 锁定状态
     * 1-锁定 2-解锁 3-扣减
     */
    private Integer lockStatus;
}
