package com.task.mgmt.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequest {

  @NotBlank(message = "The comments are required")
  private String comments;
}
