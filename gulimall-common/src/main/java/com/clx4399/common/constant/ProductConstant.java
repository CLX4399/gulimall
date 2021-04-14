package com.clx4399.common.constant;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 品牌模块常量类
 * @date 2020-11-15 23:18:30
 */
public class ProductConstant {

    public enum AttrEnum{
        ATTR_TYPE_BASE(1,"基本属性"),ATTR_TYPE_SALE(0,"销售属性");

        AttrEnum(int code, String message) {
            this.code = code;
            this.message = message;
        }

        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    public enum ProductStatusEnum{
        SPU_UP(1,"上架"),
        SPU_DOWN(0,"下架");

        ProductStatusEnum(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        private Integer code;
        private String message;

        public Integer getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }


}
