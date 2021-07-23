package com.clx4399.gulimall.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 首页控制器
 * @date 2021-07-21 18:59:57
 */
@Controller
public class IndexController {

    @GetMapping("/login.html")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/reg.html")
    public String regPage(){
        return "reg";
    }
}
