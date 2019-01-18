package com.sheen.pc.exception;

import com.sheen.pc.tip.ErrorTip;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by zxj7044 on 2018-11-6.
 */
@Slf4j
@ControllerAdvice
public class BaseControllerExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ErrorTip getBusinessException(BusinessException e) {
        log.error("业务错误: {} ", e.getMessage());
        return new ErrorTip(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorTip getRuntimeException(Exception e) {
        log.error("运行时错误: {} ", e.getMessage());
        return new ErrorTip(ResponseExceptionEnum.FAILURE.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorTip getValidationException(Exception e) {
        log.error("验证时错误: {}", e.getMessage());
        return new ErrorTip(ResponseExceptionEnum.FAILURE.getCode(), e.getMessage());
    }
}
