package com.clx4399.gulimall.order.web;

import com.clx4399.gulimall.order.service.OmsOrderService;
import com.clx4399.gulimall.order.vo.OrderConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.ExecutionException;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-08-19 19:32:43
 */
@Controller
public class OrderWebController {

    @Autowired
    OmsOrderService orderService;

    @GetMapping("/toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = orderService.confirmOrder();
        //展示订单确认的数据
        model.addAttribute("orderConfirmData", confirmVo);
        return "confirm";
    }

}
