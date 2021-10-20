package com.xkazxx.shirodemo.exception;

/**
 * @author created by xkazxx
 * @version v0.0.1
 * description: com.xkazxx.shirodemo.exception
 * date:2021/8/26
 */
public class BusinessException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  private int code;

  public BusinessException(String message, int code) {
    super(message);
    this.code = code;
  }

  public BusinessException(String message, Throwable cause, int code) {
    super(message, cause);
    this.code = code;
  }

  public BusinessException(Throwable cause, int code) {
    super(cause);
    this.code = code;
  }

  protected BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.code = code;
  }
}
