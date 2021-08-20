package com.clx4399.gulimall.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-07-19 20:09:34
 */
@ConfigurationProperties(prefix = "gulimall.thread")
@Component
@Data
public class ThreadPoolConfigProperties {

    private Integer corePoolSize;
    private Integer maximumPoolSize;
    private Long keepAliveTime;
    private Integer capacity;

}
