package com.clx4399.gulimall.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-07-15 18:30:14
 */
@Data
public class SpuItemAttrGroupVo {
    private String groupName;
    private List<SpuBaseAttrVO> attrs;
}
