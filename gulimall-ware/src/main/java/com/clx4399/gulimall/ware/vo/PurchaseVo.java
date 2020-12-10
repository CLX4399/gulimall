package com.clx4399.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 合并需求单vo
 * @date 2020-12-07 17:31:35
 */
@Data
public class PurchaseVo {

    //: 1, //整单id
    private Long purchaseId;

    //:[1,2,3,4] //合并项集合
    private List<Long> items;

}
