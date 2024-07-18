package com.task.mgmt.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskRequest {

  @NotBlank(message = "The title is required")
  private String title;
  private String description;
  private LocalDateTime dueDate;
  private Long assigneeId;
}
