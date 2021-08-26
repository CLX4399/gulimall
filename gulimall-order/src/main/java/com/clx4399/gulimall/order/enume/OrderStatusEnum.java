package com.clx4399.gulimall.order.enume;

/**
 * @description:
 * @author: wei-xhh
 * @create: 2020-07-27
 */
public enum OrderStatusEnum {

    CANCEL(4, "已取消"), CREATE_NEW(0, "待付款"),
    PAYED(1, "已付款"), RECEIVED(3, "已完成"),
    SENDED(2, "已发货"), SERVICED(6, "售后完成"),
    SERVICING(5, "售后中");
    private int code;
    private String message;

    OrderStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
