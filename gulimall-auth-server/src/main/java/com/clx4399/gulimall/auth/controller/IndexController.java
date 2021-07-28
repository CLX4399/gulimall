package com.clx4399.gulimall.auth.controller;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.clx4399.common.utils.R;
import com.clx4399.gulimall.auth.feign.ThridPartyFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 首页控制器
 * @date 2021-07-21 18:59:57
 */
@Controller
public class IndexController {

    @Autowired
    private ThridPartyFeignService thridPartyFeignService;

    @GetMapping("/sms/sendCode")
    @ResponseBody
    public R sendCode(@RequestParam("phone")String phone){
        String code = RandomUtil.randomNumbers(6);
        thridPartyFeignService.sendCode(phone,code);
        return R.ok();
    }

    @GetMapping("/login.html")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/reg.html")
    public String regPage(){
        return "reg";
    }
}
