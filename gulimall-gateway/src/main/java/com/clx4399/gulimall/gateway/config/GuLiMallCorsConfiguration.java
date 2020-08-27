package com.clx4399.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 网关跨域配置
 * @date 2020-08-19 16:18:30
 */
@Configuration
public class GuLiMallCorsConfiguration {

    /**
     * @param
     * @return org.springframework.web.cors.reactive.CorsWebFilter
     * @author CLX
     * @describe: 跨配配置拦截器
     * @date 2020/8/19 16:19
     */
    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsWebFilter(source);
    }


}
