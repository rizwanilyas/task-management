package com.task.mgmt.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
  NOT_IMPLEMENTED("NOT_IMPLEMENTED", "not any implementation exist"),
  USER_NOT_FOUND("USER_NOT_FOUND", "user not found"),
  ROLE_NOT_FOUND("ROLE_NOT_FOUND", "role not found"),
  EMAIL_OR_USERNAME_ALREADY_EXIST(
      "EMAIL_OR_USERNAME_ALREADY_EXIST", "user with the same email or username already exist"),
  INVALID_TOKEN("INVALID_TOKEN", "Invalid token provided"),
  TOKEN_EXPIRED("TOKEN_EXPIRED", "Token expired"),
  UNAUTHORIZED_REQUEST("UNAUTHORIZED_REQUEST", "UNAUTHORIZED_REQUEST"),
  TASK_NOT_FOUND("TASK_NOT_FOUND", "task doesn't exist"),
  TASK_COMMENT_NOT_FOUND("TASK_COMMENT_NOT_FOUND", "task comment doesn't exist");


  private final String code;
  private final String message;
}
