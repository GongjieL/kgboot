package com.gjie.kgboot.web.globalprocessor;

import com.alibaba.fastjson.JSON;
import com.gjie.kgboot.common.exception.BaseException;
import com.gjie.kgboot.web.response.BaseWebResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * controller异常处理
 */
@RestControllerAdvice(basePackages = {"com.gjie.kgboot.web.controller"})
public class GlobalExceptionHandler {


    private Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler({Exception.class})
    public BaseWebResponse handleMethodArgumentNotValidExceptionHandler(Exception ex) {
        String message = StringUtils.EMPTY;
        if (ex instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            List<String> list = new ArrayList<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                list.add(fieldError.getDefaultMessage());
            }
            message = JSON.toJSONString(list);
        } else if (ex instanceof BaseException) {
            message = ((BaseException) ex).getError().getDesc();
        } else if (ex instanceof MissingServletRequestParameterException) {
            message = ((MissingServletRequestParameterException) ex).getMessage();
        }
        logger.error(ex);
        return BaseWebResponse.builder()
                .success(false)
                .message(message)
                .code(500)
                .build();
    }

}
