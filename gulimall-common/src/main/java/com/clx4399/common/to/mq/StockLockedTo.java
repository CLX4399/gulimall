package com.clx4399.common.to.mq;

import lombok.Data;


/**
 * @description:
 * @author: wei-xhh
 * @create: 2020-07-30
 */
@Data
public class StockLockedTo {
    private Long id; //库存工作单id
    private StockDetailTo detail; //工作单详情id
}
