package com.example.search.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.clx4399.common.to.SkuEsModel;
import com.example.search.search.config.GuLiMallElasticsreachConfig;
import com.example.search.search.constant.EsConstant;
import com.example.search.search.service.MallSearchProductService;
import com.example.search.search.vo.SearchParam;
import com.example.search.search.vo.SearchResult;
import com.sun.javafx.tk.TKSystemMenu;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 商品搜索实现
 * @date 2021-04-23 17:02:05
 */
@Slf4j
@Service
public class MallSearchProductServiceImpl implements MallSearchProductService {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient client;

    @Override
    public SearchResult getProductResult(SearchParam searchParam) {
        /*1.动态构建商品查询DSL语句*/
        SearchResult searchResult = null;
        SearchRequest searchRequest = BuildSreachRequest(searchParam);

        /*2.执行索引请求*/
        try {
            log.info(searchRequest.toString());
            SearchResponse searchResponse = client.search(searchRequest, GuLiMallElasticsreachConfig.COMMON_OPTIONS);
            //es响应数据封装
            searchResult = buildSearchResult(searchResponse,searchParam);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*3.分析响应信息封装，将数据封装入SearchResult*/


        return searchResult;
    }

    /**
     * @param searchParam
     * @return org.elasticsearch.action.search.SearchRequest
     * @author CLX
     * @describe: 构建es请求
     * @date 2021/4/25 15:10
     * keyword=小米&
     * sort=saleCount_desc/asc&
     * hasStock=0/1&
     * skuPrice=400_1900&
     * brandId=1&
     * catalog3Id=1&
     * attrs=1_3G:4G:5G&
     * attrs=2_骁龙845&
     * attrs=4_高清屏
     */
    private SearchRequest BuildSreachRequest(SearchParam searchParam) {
        //用于构建DSL语句
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //用于bool query
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //1.1bool must
        if (StringUtils.isNotBlank(searchParam.getKeyword())){
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle",searchParam.getKeyword()));
        }

        //1.2 bool filter
        if (searchParam.getCatalog3Id()!=null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId",searchParam.getCatalog3Id()));
        }
        if (!CollectionUtils.isEmpty(searchParam.getBrandId())){
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId",searchParam.getBrandId()));
        }
        if (searchParam.getHasStock()!=null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock",searchParam.getHasStock()==1));
        }
        if (StringUtils.isNotBlank(searchParam.getSkuPrice())){
            RangeQueryBuilder skuPrice = QueryBuilders.rangeQuery("skuPrice");
            String[] prices = searchParam.getSkuPrice().split("_");
            if (prices.length == 1){
                if (searchParam.getSkuPrice().startsWith("_")){
                    skuPrice.gte(Integer.parseInt(prices[0]));
                }else {
                    skuPrice.lte(Integer.parseInt(prices[0]));
                }
            }else {
                //_6000会截取成["","6000"]
                if (!prices[0].isEmpty()) {
                    skuPrice.gte(Integer.parseInt(prices[0]));
                }
                skuPrice.lte(Integer.parseInt(prices[1]));
            }
            boolQueryBuilder.filter(skuPrice);
        }

        if (!CollectionUtils.isEmpty(searchParam.getAttrs())){
            BoolQueryBuilder attrQueryBuilder = new BoolQueryBuilder();
            for (String attr : searchParam.getAttrs()) {
                String[] s = attr.split("_");
                attrQueryBuilder.must(QueryBuilders.termQuery("attrs.attrId",s[0]));
                String[] split = s[1].split(":");
                attrQueryBuilder.must(QueryBuilders.matchQuery("attrs.attrValue",split));
            }
            NestedQueryBuilder attrs = QueryBuilders.nestedQuery("attrs", attrQueryBuilder, ScoreMode.None);
            boolQueryBuilder.filter(attrs);
        }
        /*集成完查询语句*/
        searchSourceBuilder.query(boolQueryBuilder);

        if (StringUtils.isNotBlank(searchParam.getSort())){
            String[] split = searchParam.getSort().split("_");
            searchSourceBuilder.sort(split[0], split[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC);
        }
        searchSourceBuilder.from((searchParam.getPageNum()-1)*EsConstant.PRODUCT_PAGESIZE);
        searchSourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);

        if (StringUtils.isNotBlank(searchParam.getKeyword())){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }

        /*聚合品牌信息*/
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brandAgg").field("brandId");
        TermsAggregationBuilder brandNameAgg = AggregationBuilders.terms("brandNameAgg").field("brandName");
        TermsAggregationBuilder brandImgAgg = AggregationBuilders.terms("brandImgAgg").field("brandImg");
        brandAgg.subAggregation(brandImgAgg);
        brandAgg.subAggregation(brandNameAgg);
        searchSourceBuilder.aggregation(brandAgg);

        /*聚合分类信息*/
        TermsAggregationBuilder catalogAgg = AggregationBuilders.terms("catalogAgg").field("catalogId");
        TermsAggregationBuilder catalogNameAgg = AggregationBuilders.terms("catalogNameAgg").field("catalogName");
        catalogAgg.subAggregation(catalogNameAgg);
        searchSourceBuilder.aggregation(catalogAgg);

        /*聚合产品属性信息*/
        NestedAggregationBuilder attrsAgg = new NestedAggregationBuilder("attrs", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId");
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms("attrNameAgg").field("attrs.attrName");
        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue");
        attrIdAgg.subAggregation(attrNameAgg);
        attrIdAgg.subAggregation(attrValueAgg);
        attrsAgg.subAggregation(attrIdAgg);
        searchSourceBuilder.aggregation(attrsAgg);

        SearchRequest request = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX.getIndex()}, searchSourceBuilder);
        return request;
    }

    /**
     * @param searchResponse
	 * @param searchParam
     * @return com.example.search.search.vo.SearchResult
     * @author CLX
     * @describe: es响应数据封装
     * @date 2021/4/25 15:14
     */
    private SearchResult buildSearchResult(SearchResponse searchResponse, SearchParam searchParam) {
        SearchResult searchResult = new SearchResult();

        SearchHits hits = searchResponse.getHits();

        /*1.封装查询到的商品信息*/
        if (hits.getTotalHits().value>0){
            ArrayList<SkuEsModel> skuEsModels = new ArrayList<>();
            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel skuEsModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                /*设置标题高亮*/
                if (StringUtils.isNotBlank(searchParam.getKeyword())){
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String string = skuTitle.getFragments()[0].toString();
                    skuEsModel.setSkuTitle(string);
                }
                skuEsModels.add(skuEsModel);
            }
            searchResult.setProducts(skuEsModels);
        }
        /*2.封装分页信息*/
        /*当前页码*/
        searchResult.setPageNum(searchParam.getPageNum());
        /*总记录数*/
        long total = hits.getTotalHits().value;
        searchResult.setTotal(total);
        /*总页数*/
        Integer totalPages = (int)total % EsConstant.PRODUCT_PAGESIZE == 0 ?
                (int)total / EsConstant.PRODUCT_PAGESIZE : (int)total / EsConstant.PRODUCT_PAGESIZE + 1;
        searchResult.setTotalPages(totalPages);


        /*3.封装商品相关联品牌*/
        ArrayList<SearchResult.BrandVo> brandVos = new ArrayList<>();
        Aggregations aggregations = searchResponse.getAggregations();
        ParsedLongTerms brandAgg = aggregations.get("brandAgg");
        for (Terms.Bucket bucket : brandAgg.getBuckets()) {
            long brandId = bucket.getKeyAsNumber().longValue();
            Aggregations bucketAggregations = bucket.getAggregations();
            /*获取商品名称*/
            ParsedStringTerms brandNameAgg = bucketAggregations.get("brandNameAgg");
            String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();
            /*获取商品图片地址*/
            ParsedStringTerms brandImgAgg = bucketAggregations.get("brandImgAgg");
            String brandImg = brandImgAgg.getBuckets().get(0).getKeyAsString();

            SearchResult.BrandVo brandVo = new SearchResult.BrandVo(brandId, brandName, brandImg);
            brandVos.add(brandVo);
        }
        searchResult.setBrands(brandVos);

        /*4.封装商品的分类信息*/
        ArrayList<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        ParsedLongTerms catalogAgg = aggregations.get("catalogAgg");
        for (Terms.Bucket bucket : catalogAgg.getBuckets()) {
            long catalogId = bucket.getKeyAsNumber().longValue();
            Aggregations bucketAggregations = bucket.getAggregations();
            ParsedStringTerms catalogNameAgg = bucketAggregations.get("catalogNameAgg");
            String catalogName = catalogNameAgg.getBuckets().get(0).getKeyAsString();
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo(catalogId, catalogName);
            catalogVos.add(catalogVo);
        }
        searchResult.setCatalogs(catalogVos);

        /*5.查询商品可做查询的属性*/
        ArrayList<SearchResult.AttrVo> attrVos = new ArrayList<>();
        ParsedNested attrs = aggregations.get("attrs");
        ParsedLongTerms  attrIdAgg = attrs.getAggregations().get("attrIdAgg");
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            long attrId = bucket.getKeyAsNumber().longValue();
            Aggregations attraggres = bucket.getAggregations();
            ParsedStringTerms attrNameAgg = attraggres.get("attrNameAgg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            ParsedStringTerms attrValueAgg = attraggres.get("attrValueAgg");
            ArrayList<String> attrValues = new ArrayList<>();
            for (Terms.Bucket attrValueAggBucket : attrValueAgg.getBuckets()) {
                String keyAsString = attrValueAggBucket.getKeyAsString();
                attrValues.add(keyAsString);
            }
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo(attrId, attrName, attrValues);
            attrVos.add(attrVo);
        }
        searchResult.setAttrs(attrVos);

        return  searchResult;
     }


}
