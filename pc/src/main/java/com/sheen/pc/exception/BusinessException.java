package com.sheen.pc.exception;

import lombok.Data;

/**
 * Created by zxj7044 on 2018-11-7.
 */
@Data
public class BusinessException extends RuntimeException {
    private Integer code;
    private String message;

    public BusinessException(ServiceException serviceException) {
        this.code = serviceException.getCode();
        this.message = serviceException.getMessage();
    }

    public BusinessException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
