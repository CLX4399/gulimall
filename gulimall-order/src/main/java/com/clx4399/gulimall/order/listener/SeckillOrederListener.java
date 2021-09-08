package com.clx4399.gulimall.order.listener;

import com.clx4399.common.to.mq.SeckillOrderTo;
import com.clx4399.gulimall.order.service.OmsOrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
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
 * @date 2021-09-07 16:43:45
 */
@Slf4j
@Component
@RabbitListener(queues = "order.seckill.order.queue")
public class SeckillOrederListener {

    @Autowired
    OmsOrderService orderService;

    @RabbitHandler
    public void listener(SeckillOrderTo seckillOrder, Channel channel, Message message) throws IOException {

        try {
            log.info("准备创建秒杀单的详细信息。。");
            orderService.createSeckillOrder(seckillOrder);
            //手动接消息
            //手动调用支付宝收单

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }

}
