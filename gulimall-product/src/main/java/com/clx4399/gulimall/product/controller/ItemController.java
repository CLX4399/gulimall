package com.clx4399.gulimall.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-05-11 10:41:36
 */
@Controller
public class ItemController {

    /**
     * @param
     * @return java.lang.String
     * @author CLX
     * @describe:
     * @date 2021/5/11 10:42
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(){

        return "item";
    }

}
