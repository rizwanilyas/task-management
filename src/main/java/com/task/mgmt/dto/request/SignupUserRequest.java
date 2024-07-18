package com.task.mgmt.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignupUserRequest {

  @NotEmpty(message = "First Name is required")
  private String firstName;

  private String lastName;

  @NotEmpty(message = "Email is required")
  @Email(message = "Valid email is required")
  private String email;

  @NotEmpty(message = "username is required")
  private String username;

  @NotEmpty(message = "password")
  private String password;

}
