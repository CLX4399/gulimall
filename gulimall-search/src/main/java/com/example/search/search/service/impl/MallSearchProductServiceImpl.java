package com.example.search.search.service.impl;

import com.example.search.search.service.MallSearchProductService;
import com.example.search.search.vo.SearchParam;
import com.example.search.search.vo.SearchResult;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 商品搜索实现
 * @date 2021-04-23 17:02:05
 */
@Service
public class MallSearchProductServiceImpl implements MallSearchProductService {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient client;

    @Override
    public SearchResult getProductResult(SearchParam searchParam) {
        /*1.动态构建商品查询DSL语句*/

        /*2.执行索引请求*/

        /*3.分析响应信息封装，将数据封装入SearchResult*/

        return null;
    }
}
