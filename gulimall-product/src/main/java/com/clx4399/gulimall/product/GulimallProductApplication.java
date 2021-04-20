package com.clx4399.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author WhtCl
 */
@EnableDiscoveryClient
@MapperScan("com.clx4399.gulimall.product.dao")
@EnableFeignClients("com.clx4399.gulimall.product.feign")
@SpringBootApplication
@EnableCaching
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
