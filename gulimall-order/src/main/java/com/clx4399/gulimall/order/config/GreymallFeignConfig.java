package com.clx4399.gulimall.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: feign远程调用cookie拦截配置
 * @date 2021-08-23 11:36:09
 */
@Configuration
public class  GreymallFeignConfig {

    /**
     * @param
     * @return RequestInterceptor
     * @author CLX
     * @describe: 拦截远程调用携带头信息
     * @date 2021/8/23 11:37
     */
    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                if (attributes!=null){
                    HttpServletRequest request = attributes.getRequest();
                    String cookie = request.getHeader("Cookie");
                    requestTemplate.header("Cookie",cookie);
                }
            }
        };
    }
}
