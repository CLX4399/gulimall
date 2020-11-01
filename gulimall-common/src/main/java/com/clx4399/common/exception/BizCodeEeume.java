package com.clx4399.common.exception;

import org.omg.CORBA.UNKNOWN;

/**
 * 异常编码规范
 * @author WhtCl
 * */
public enum BizCodeEeume {

    VAILD_EXCEPTION(10000,"系统未知异常"),

    UNKNOWN_EXCEPTION(10001,"参数格式校验异常");

    private int code;
    private String message;

    BizCodeEeume(int code,String message){
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
