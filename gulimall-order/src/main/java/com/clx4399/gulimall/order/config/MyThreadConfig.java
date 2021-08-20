package com.clx4399.gulimall.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-07-19 20:04:54
 */
//@EnableConfigurationProperties({ThreadPoolConfigProperties.class})
@Configuration
public class MyThreadConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties pool){
        return  new ThreadPoolExecutor(pool.getCorePoolSize(),
                pool.getMaximumPoolSize(),
                pool.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(pool.getCapacity()),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
                );
    }

}
