package com.example.search.search.service;

import com.example.search.search.vo.SearchParam;
import com.example.search.search.vo.SearchResult;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 商品搜索
 * @date 2021-04-23 17:01:36
 */
public interface MallSearchProductService {

    /**
     * @param
     * @return com.example.search.search.vo.SearchResult
     * @author CLX
     * @describe: 获取es查询结果
     * @date 2021/4/23 17:03
     */
    SearchResult getProductResult(SearchParam searchParam);

}
