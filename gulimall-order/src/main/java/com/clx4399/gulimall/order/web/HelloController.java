package com.clx4399.gulimall.order.web;

import com.clx4399.gulimall.order.entity.OmsOrderEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.UUID;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-08-19 15:37:58
 */
@Controller
public class HelloController {

    @GetMapping("/{page}.html")
    public String listPage(@PathVariable String page){
        return page;
    }


    @Autowired
    RabbitTemplate rabbitTemplate;

    @ResponseBody
    @GetMapping("/test/createOrder")
    public String createOrderTest(){
        //订单下单成功
        OmsOrderEntity entity = new OmsOrderEntity();
        entity.setOrderSn(UUID.randomUUID().toString());
        entity.setModifyTime(new Date());
//        System.out.println(entity);
        //给MQ发消息
        rabbitTemplate.convertAndSend("order-event-exchange","order.create.order",entity);
        return "OK";
    }

}
