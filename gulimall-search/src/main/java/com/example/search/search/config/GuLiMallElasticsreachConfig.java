package com.example.search.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: es客户端配置
 * @date 2020-12-22 10:16:24
 */
@Configuration
public class GuLiMallElasticsreachConfig {

    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        /*builder.addHeader("Authorization", "Bearer " + TOKEN);
        builder.setHttpAsyncResponseConsumerFactory(
                new HttpAsyncResponseConsumerFactory
                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));*/
        COMMON_OPTIONS = builder.build();
    }

    @Bean
     public RestHighLevelClient restHighLevelClient(){
         RestClientBuilder restClientBuilder =
                 RestClient.builder(new HttpHost("139.224.113.89",9200,"http"));
         RestHighLevelClient builder = new RestHighLevelClient(restClientBuilder);
         return builder;
     }

}
