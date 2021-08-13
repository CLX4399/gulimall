package com.clx4399.gulimall.cart.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: Redisson配置
 * @date 2021-04-19 11:45:51
 */
@Configuration
public class RedissonConfig {

    @Value("${ipAddr}")
    private String ipAddr;

    // redission通过redissonClient对象使用 // 如果是多个redis集群，可以配置
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        // 创建单例模式的配置
        config.useSingleServer().setAddress("redis://" + ipAddr + ":6379").setPassword("clx4399");
        return Redisson.create(config);
    }
}
