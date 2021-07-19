package com.clx4399.gulimall.product.controller;

import com.clx4399.gulimall.product.service.SkuInfoService;
import com.clx4399.gulimall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-05-11 10:41:36
 */
@Controller
public class ItemController {

    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * @param
     * @return java.lang.String
     * @author CLX
     * @describe:
     * @date 2021/5/11 10:42
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable String skuId, Model model){
        SkuItemVo skuItemVo = skuInfoService.item(skuId);
        model.addAttribute("item",skuItemVo);
        return "item";
    }

}
