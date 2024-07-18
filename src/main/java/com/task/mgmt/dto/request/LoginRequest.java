package com.task.mgmt.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {

  @NotEmpty(message = "password is required")
  private String password;

  @NotEmpty(message = "username is required")
  private String username;
}
