package com.example.search.search.vo;

import com.clx4399.common.to.SkuEsModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-04-22 15:58:42
 * <p>Title: SearchResponse</p>
 * Description：包含页面需要的所有信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {

    /**
     * 查询到的所有商品信息
     */
    private List<SkuEsModel> products;

    /*** 当前页码*/
    private Integer pageNum;
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 总页码
     */
    private Integer totalPages;

    /**
     * 当前查询到的结果, 所有涉及到的品牌
     */
    private List<BrandVo> brands;

    /**
     * 当前查询到的结果, 所有涉及到的分类
     */
    private List<CatalogVo> catalogs;

    /**
     * 当前查询的结果 所有涉及到所有属性
     */
    private List<AttrVo> attrs;

    /**
     * 导航页   页码遍历结果集(分页)
     */
    private List<Integer> pageNavs;

//	================以上是返回给页面的所有信息================

    /**
     * 导航数据
     */
    private List<NavVo> navs = new ArrayList<>();

    /**
     * 便于判断当前id是否被使用
     */
    private List<Long> attrIds = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NavVo {
        private String name;
        private String navValue;
        private String link;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BrandVo {

        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CatalogVo {
        private Long catalogId;
        private String catalogName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AttrVo {

        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
}
