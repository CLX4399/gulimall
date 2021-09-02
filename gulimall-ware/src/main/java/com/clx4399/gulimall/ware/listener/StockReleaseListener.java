package com.clx4399.gulimall.ware.listener;

import com.clx4399.common.to.mq.OrderTo;
import com.clx4399.common.to.mq.StockLockedTo;
import com.clx4399.gulimall.ware.service.WareSkuService;
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
 * @description: MQ监听消费
 * @date 2021-08-31 19:56:10
 */
@Slf4j
@Component
@RabbitListener(queues = "stock.release.stock.queue")
public class StockReleaseListener {

    @Autowired
    WareSkuService wareSkuService;

    @RabbitHandler
    public void handlerStockLockedRelease(StockLockedTo to, Message message, Channel channel) throws IOException {
        log.info("库存释放队列消息接收***");
        try {
            wareSkuService.unLockStock(to);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }

    @RabbitHandler
    public void handlerStockOrderCloseRelease(OrderTo to, Message message, Channel channel) throws IOException {
        log.info("订单关闭库存释放队列消息接收***");
        try {
            wareSkuService.unLockStock(to);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }

}
