package com.task.mgmt.exception;

import com.task.mgmt.constant.ErrorCode;
import org.springframework.http.HttpStatus;

public class BadRequestException extends AppException {

  public BadRequestException(ErrorCode errorCode) {
    super(HttpStatus.BAD_REQUEST, errorCode);
  }
}
