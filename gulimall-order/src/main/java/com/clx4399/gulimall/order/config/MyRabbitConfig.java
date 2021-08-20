package com.clx4399.gulimall.order.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: RabbitMQ扩展配置类
 * @date 2021-08-16 17:55:14
 */
@Configuration
public class MyRabbitConfig {

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}
