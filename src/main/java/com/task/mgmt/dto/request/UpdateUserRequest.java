package com.task.mgmt.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserRequest {

  @NotEmpty(message = "First Name is required")
  private String firstName;

  private String lastName;

  @NotNull(message = "at least one role is required")
  private Set<Long> roles;
}
