package com.clx4399.gulimall.order.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.ExecutionException;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-08-19 19:32:43
 */
public class OrderWebController {

    @GetMapping("/toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
        /*OrderConfirmVo confirmVo = orderService.confirmOrder();
        //展示订单确认的数据
        model.addAttribute("orderConfirmData", confirmVo);*/
        return "confirm";
    }

}
