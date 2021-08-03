package com.clx4399.common.exception;

/**
 * 异常编码规范
 * @author WhtCl
 * */
public enum BizCodeEnum {

    VAILD_EXCEPTION(10001,"参数格式校验异常"),

    UNKNOWN_EXCEPTION(10000,"系统未知异常"),

    SMS_CODE_EXCEPTION(10002,"短息发送频繁，请稍后再试"),

    PRODUCT_UP_EXCEPTION(500,"es数据异常"),

    USER_EXIST_EXCEPTION(15001, "存在相同的用户"),

    PHONE_EXIST_EXCEPTION(15002, "存在相同的手机号"),

    NO_STOCK_EXCEPTION(21000, "商品库存不足"),

    LOGIN_ACCOUNT_PASSWORD_EXCEPTION(15003, "账号或密码错误");

    private int code;
    private String message;

    BizCodeEnum(int code,String message){
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
