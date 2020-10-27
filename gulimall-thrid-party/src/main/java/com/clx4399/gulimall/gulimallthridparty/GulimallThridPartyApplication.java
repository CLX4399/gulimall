package com.clx4399.gulimall.gulimallthridparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GulimallThridPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallThridPartyApplication.class, args);
    }

}
