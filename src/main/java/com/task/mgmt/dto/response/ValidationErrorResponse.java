package com.task.mgmt.dto.response;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {

  private Map<String, String> errors;
  private String message;
  private HttpStatus status;
  private LocalDateTime dateTime;
}
