package com.clx4399.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2020-12-09 18:35:52
 */
@Data
public class PurchaseDoneVo {

    private Long id;

    private List<PurchaseDoneDetailVo> items;

}
