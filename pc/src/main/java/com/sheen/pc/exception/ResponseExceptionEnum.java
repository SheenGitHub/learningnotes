package com.sheen.pc.exception;

import lombok.Data;

/**
 * Created by zxj7044 on 2018-11-6.
 */

public enum ResponseExceptionEnum implements ServiceException {

    SUCCESS(0x0000, "成功"),

    FAILURE(0xFFFF, "失败"),

    USER_NOT_LOGIN(0x1005, "用户没有登录"),

    TICKET_INVALID(0x0191, "ticket无效"),

    NOT_FOUND(0x1000, "没有找到");

    ResponseExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
