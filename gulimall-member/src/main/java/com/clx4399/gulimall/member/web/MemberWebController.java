package com.clx4399.gulimall.member.web;

import com.clx4399.common.utils.R;
import com.clx4399.gulimall.member.feign.OrderFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-09-02 20:24:21
 */
@Controller
public class MemberWebController {

    @Autowired
    OrderFeignService orderFeignService;

    @GetMapping("/memberOrder.html")
    public String memberOrderPage(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, Model model){
        //可获取到支付宝给我们传来的所有请求数据：
        // request:验证签名，如果正确可以去修改订单状态
        //不过最好使用异步通知

        //查出当前登录的用户的所有订单列表数据
        Map<String,Object> page = new HashMap<>();
        page.put("page",pageNum.toString());
        R r = orderFeignService.listWithItem(page);
        model.addAttribute("orders",r);

        return "orderList";
    }

}
