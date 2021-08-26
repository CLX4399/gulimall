package com.clx4399.gulimall.order.web;

import com.clx4399.gulimall.order.service.OmsOrderService;
import com.clx4399.gulimall.order.vo.OrderConfirmVo;
import com.clx4399.gulimall.order.vo.OrderSubmitVo;
import com.clx4399.gulimall.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo vo, Model model, RedirectAttributes attributes) {

        //下单：去创建订单，验令牌，验价格，锁库存
        SubmitOrderResponseVo responseVo = orderService.submitOrder(vo);
        if (responseVo.getCode() == 0) {
            //下单成功来到支付选择页
            model.addAttribute("submitOrderResp", responseVo);
            return "pay";
        } else {
            String msg = "下单失败:";
            switch (responseVo.getCode()) {
                case 1:
                    msg += "订单信息过期，请刷新再提交";
                    break;
                case 2:
                    msg += "订单商品价格发生变化，请确认后再次提交";
                    break;
                case 3:
                    msg += "库存锁定失败，商品库存不足";
                    break;
            }
            //下单失败回到订单确认页
            attributes.addFlashAttribute("msg", msg);
            return "redirect:http://order.greymall.com/toTrade";
        }
    }

}
