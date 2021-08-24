package com.clx4399.gulimall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-08-20 10:39:35
 */
@Data
public class OrderItemVo {

    private Long skuId;
    private String title;
    private String image;
    private List<String> skuAttr;
    private BigDecimal price;
    private Integer count;
    private BigDecimal totalPrice;

    //TODO 查询库存状态
    private boolean hasStock = true;
    private BigDecimal weight; //商品的重量

}
