package com.task.mgmt.advice;

import com.task.mgmt.constant.ErrorCode;
import com.task.mgmt.dto.response.ExceptionResponse;
import com.task.mgmt.dto.response.ValidationErrorResponse;
import com.task.mgmt.exception.AppException;
import io.jsonwebtoken.ExpiredJwtException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ExceptionResponse> handleAppException(AppException exception) {
    log.error(exception.getMessage(), exception);
    ErrorCode code = exception.getErrorCode();
    ExceptionResponse response =
        new ExceptionResponse(
            code.getMessage(), code.getCode(), exception.getHttpStatus(), LocalDateTime.now());
    return new ResponseEntity<>(response, exception.getHttpStatus());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ExceptionResponse> handleAccessDeniedException(
      AccessDeniedException exception) {
    log.error(exception.getMessage(), exception);
    ExceptionResponse response =
        new ExceptionResponse(
            exception.getMessage(),
            HttpStatus.UNAUTHORIZED.getReasonPhrase(),
            HttpStatus.UNAUTHORIZED,
            LocalDateTime.now());
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException exception) {
    log.error(exception.getMessage(), exception);
    ExceptionResponse response =
        new ExceptionResponse(
            exception.getMessage(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            HttpStatus.BAD_REQUEST,
            LocalDateTime.now());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ExceptionResponse> handleBadCredentialsException(
      BadCredentialsException exception) {
    log.error(exception.getMessage(), exception);
    ExceptionResponse response =
        new ExceptionResponse(
            exception.getMessage(),
            HttpStatus.UNAUTHORIZED.getReasonPhrase(),
            HttpStatus.UNAUTHORIZED,
            LocalDateTime.now());
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
  public ResponseEntity<ExceptionResponse> handleAuthenticationCredentialsNotFoundException(
      AuthenticationCredentialsNotFoundException exception) {
    log.error(exception.getMessage(), exception);
    ExceptionResponse response =
        new ExceptionResponse(
            exception.getMessage(),
            HttpStatus.UNAUTHORIZED.getReasonPhrase(),
            HttpStatus.UNAUTHORIZED,
            LocalDateTime.now());
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<ExceptionResponse> handleExpiredToken(ExpiredJwtException exception) {
    log.error(exception.getMessage(), exception);
    ExceptionResponse response =
        new ExceptionResponse(
            exception.getMessage(),
            HttpStatus.UNAUTHORIZED.getReasonPhrase(),
            HttpStatus.UNAUTHORIZED,
            LocalDateTime.now());
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });

    ValidationErrorResponse response =
        new ValidationErrorResponse(
            errors,
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            HttpStatus.BAD_REQUEST,
            LocalDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> handleGeneralException(Exception exception) {
    log.error(exception.getMessage(), exception);
    ExceptionResponse response =
        new ExceptionResponse(
            exception.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            LocalDateTime.now());
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
