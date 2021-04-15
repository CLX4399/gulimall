package com.clx4399.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 品牌模块常量类
 * @date 2020-11-15 23:18:30
 */
public class ProductConstant {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum AttrEnum{
        ATTR_TYPE_BASE(1,"基本属性"),ATTR_TYPE_SALE(0,"销售属性");
        private int code;
        private String message;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum ProductStatusEnum{
        SPU_UP(1,"上架"),
        SPU_DOWN(0,"下架");
        private Integer code;
        private String message;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum ProductCatelogLevelEnum{
        ONE(1,"一级分类"),
        TWO(2,"二级分类"),
        THREE(3,"三级分类");
        private Integer code;
        private String message;

    }


}
