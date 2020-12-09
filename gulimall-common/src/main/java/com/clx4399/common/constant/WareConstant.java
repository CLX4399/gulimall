package com.clx4399.common.constant;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 库存服务常量
 * @date 2020-12-07 17:36:05
 */
public class WareConstant {

    public enum PurchaseEnum{
        CREATED(0,"新增"),
        ALLOCATED(1,"已分配"),
        RECEIVED(2,"已领取"),
        COMPLETED(3,"已完成"),
        ABNORMAL(4,"有异常");

        PurchaseEnum(Integer code, String message) {
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

    public enum PurchaseDetailEnum{
        CREATED(0,"新增"),
        ALLOCATED(1,"已分配"),
        PURSHSAEING(2,"正在采购"),
        COMPLETED(3,"已完成"),
        PURCHASEFAIL(4,"采购失败");

        PurchaseDetailEnum(Integer code, String message) {
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
