package com.clx4399.gulimall.order.web;

import com.alipay.api.AlipayApiException;
import com.clx4399.gulimall.order.config.AlipayTemplate;
import com.clx4399.gulimall.order.service.OmsOrderService;
import com.clx4399.gulimall.order.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description:
 * @author: wei-xhh
 * @create: 2020-07-31
 */
@Controller
public class PayWebController {

    @Autowired
    AlipayTemplate alipayTemplate;

    @Autowired
    OmsOrderService orderService;

    /**
     * 将支付页让浏览器展示，
     * 支付成功以后，要跳到用户的订单列表页
     * @param orderSn
     * @return
     * @throws AlipayApiException
     */
    @ResponseBody
    @GetMapping(value = "/payOrder",produces = "text/html")
    public String payOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {
        PayVo payVo = orderService.getOrderPay(orderSn);
        String pay = alipayTemplate.pay(payVo);
        return pay;
    }
}
