package com.task.mgmt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto {

  @NotBlank(message = "The token is required")
  private String token;

  @Size(min = 6, message = "Must be at least 6 characters")
  @NotBlank(message = "This field is required")
  private String password;

  @NotBlank(message = "This field is required")
  private String confirmPassword;
}
