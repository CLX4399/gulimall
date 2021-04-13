package com.example.search.search;

import com.alibaba.fastjson.JSON;
import com.clx4399.common.to.MemberPrice;
import com.example.search.search.config.GuLiMallElasticsreachConfig;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@SpringBootTest
class GulimallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    void esSearch() throws IOException {

        SearchRequest searchRequest = new SearchRequest("newbank");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("address","Mill"));
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, GuLiMallElasticsreachConfig.COMMON_OPTIONS);
        log.info(search.toString());
    }

    @Test
    void contextLoads() throws IOException {

        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        MemberPrice user = new MemberPrice();
        user.setId(11L);
        user.setName("12312");
        user.setPrice(new BigDecimal("123123"));

        String toJSONString = JSON.toJSONString(user);
        indexRequest.source(toJSONString, XContentType.JSON);
        IndexResponse index = restHighLevelClient.index(indexRequest, GuLiMallElasticsreachConfig.COMMON_OPTIONS);
        log.info(index.toString());
    }

    @Test
    void find() throws IOException {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bank");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchQuery("address","mill"));

        TermsAggregationBuilder aggreAge = AggregationBuilders.terms("agg1").field("age").size(10);

        searchSourceBuilder.aggregation(aggreAge);

        AvgAggregationBuilder aggBalance = AggregationBuilders.avg("agg2").field("balance");

        searchSourceBuilder.aggregation(aggBalance);

        searchRequest.source(searchSourceBuilder);

        log.info("检索条件：{}",searchSourceBuilder.toString());

        SearchResponse search = restHighLevelClient.search(searchRequest, GuLiMallElasticsreachConfig.COMMON_OPTIONS);

        log.info("查询结果：{}",search.toString());
    }

}
