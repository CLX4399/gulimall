package com.example.search.search.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-04-14 11:48:57
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum EsConstant {


    PRODUCT_INDEX("product","商品检索索引");

    private String index;

    private String message;

}
