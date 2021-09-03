package com.clx4399.gulimall.order.listener;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.clx4399.gulimall.order.config.AlipayTemplate;
import com.clx4399.gulimall.order.service.OmsOrderService;
import com.clx4399.gulimall.order.vo.PayAsyncVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 订单支付监听
 * @date 2021-09-02 20:44:34
 */
@RestController
public class OrderPayedListener {

    @Autowired
    OmsOrderService orderService;

    @Autowired
    AlipayTemplate alipayTemplate;

    @PostMapping("/payed/notify")
    public String orderPayedCheck(PayAsyncVo vo, HttpServletRequest request) throws AlipayApiException {
        //验签
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayTemplate.getAlipay_public_key(), alipayTemplate.getCharset(), alipayTemplate.getSign_type());
        if (signVerified) {
            //签名验证成功
            System.out.println("签名验证成功...");
            String result = orderService.handlePayResult(vo);
            return result;
        } else {
            System.out.println("签名验证失败...");
            return "error";
        }
    }

}
