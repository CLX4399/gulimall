package com.example.search.search;

import com.alibaba.fastjson.JSON;
import com.clx4399.common.to.MemberPrice;
import com.example.search.search.config.GuLiMallElasticsreachConfig;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@Slf4j
@SpringBootTest
class GulimallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    void contextLoads() throws IOException {

        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        MemberPrice user = new MemberPrice();
        String toJSONString = JSON.toJSONString(user);
        indexRequest.source(toJSONString, XContentType.JSON);
        IndexResponse index = restHighLevelClient.index(indexRequest, GuLiMallElasticsreachConfig.COMMON_OPTIONS);
        log.info(index.toString());
    }

}
