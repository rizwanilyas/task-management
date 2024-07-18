package com.task.mgmt.exception;

import com.task.mgmt.constant.ErrorCode;
import org.springframework.http.HttpStatus;

public class RecordNotFoundException extends AppException {

  public RecordNotFoundException(ErrorCode errorCode) {
    super(HttpStatus.NOT_FOUND, errorCode);
  }
}
