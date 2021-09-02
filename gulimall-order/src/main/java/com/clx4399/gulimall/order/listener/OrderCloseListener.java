package com.clx4399.gulimall.order.listener;

import com.clx4399.gulimall.order.entity.OmsOrderEntity;
import com.clx4399.gulimall.order.service.OmsOrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-09-01 21:27:29
 */
@Component
@RabbitListener(queues = "order.release.order.queue")
public class OrderCloseListener {

    @Autowired
    OmsOrderService orderService;

    @RabbitHandler
    public void listener(OmsOrderEntity entity, Channel channel, Message message) throws IOException {
        System.out.println("收到过期的订单消息，准备关闭订单：" + entity.getOrderSn());
        try {
            orderService.closeOrder(entity);
            //手动接消息
            //手动调用支付宝收单

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }

}
