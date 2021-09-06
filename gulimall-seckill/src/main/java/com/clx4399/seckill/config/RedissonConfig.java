package com.clx4399.seckill.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-09-06 14:50:44
 */
@Configuration
public class RedissonConfig {

    // redission通过redissonClient对象使用 // 如果是多个redis集群，可以配置
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        // 创建单例模式的配置
        config.useSingleServer().setAddress("redis://139.224.113.89:6379").setPassword("clx4399").setDatabase(3);
        return Redisson.create(config);
    }

}
