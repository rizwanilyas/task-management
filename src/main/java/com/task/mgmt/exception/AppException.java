package com.task.mgmt.exception;

import com.task.mgmt.constant.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {

  public HttpStatus httpStatus;
  public ErrorCode errorCode;

  public AppException(HttpStatus httpStatus, ErrorCode errorCode) {
    super();
    this.errorCode = errorCode;
    this.httpStatus = httpStatus;
  }

  public AppException(ErrorCode errorCode) {
    super();
    this.errorCode = errorCode;
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
