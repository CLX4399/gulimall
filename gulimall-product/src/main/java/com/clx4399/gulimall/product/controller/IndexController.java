package com.clx4399.gulimall.product.controller;

import com.clx4399.common.constant.ProductConstant;
import com.clx4399.gulimall.product.entity.CategoryEntity;
import com.clx4399.gulimall.product.service.CategoryService;
import com.clx4399.gulimall.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 首页控制器
 * @date 2021-04-14 20:46:24
 */
@Controller()
@Slf4j
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    /**
     * @param model
     * @return java.lang.String
     * @author CLX
     * @describe: 获取首页信息
     * @date 2021/4/15 21:05
     */
    @GetMapping({"/","/index.html"})
    public String getIndex(Model model){
        List<CategoryEntity> entityList  = categoryService.getLevel1Categroies(ProductConstant.ProductCatelogLevelEnum.ONE.getCode());
        model.addAttribute("catagories",entityList);
        return "index";
    }

    /**
     * @param
     * @return java.util.Map<java.lang.String,java.util.List<com.clx4399.gulimall.product.vo.Catelog2Vo>>
     * @author CLX
     * @describe: 获取二级，三级分类
     * @date 2021/4/15 21:05
     */
    @GetMapping("/json/catalog.json")
    @ResponseBody
    public Map<String,List<Catelog2Vo>> getCatelogJson(){
        Map<String,List<Catelog2Vo>> entityList  = categoryService.getCateLogLevel2();
        return entityList;
    }

}
