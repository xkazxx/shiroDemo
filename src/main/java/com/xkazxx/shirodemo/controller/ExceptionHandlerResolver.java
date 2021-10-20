package com.xkazxx.shirodemo.controller;

import com.xkazxx.shirodemo.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author created by xkazxx
 * @version v0.0.1
 * description: com.xkazxx.shirodemo.controller
 * date:2021/8/26
 */
@RestControllerAdvice
public class ExceptionHandlerResolver {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String exceptionHandler(Exception e) {
    System.out.println("处理Exception开始");
    return e.getMessage();
  }

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String nullPointExceptionHandler(NullPointerException e) {
    System.out.println("处理NullPointerException开始");
    return e.getMessage();
  }
  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String nullBusinessExceptionHandler(BusinessException e) {
    System.out.println("处理BusinessException开始");
    return e.getMessage();
  }
}
