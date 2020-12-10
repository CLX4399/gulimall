package com.clx4399.gulimall.ware.vo;

import lombok.Data;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2020-12-09 18:35:32
 */
@Data
public class PurchaseDoneDetailVo {

    /**
     * itemId:1,status:4,reason:""
     */

    private Long itemId;

    private Integer status;

    private String reason;
}
