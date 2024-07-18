package com.task.mgmt.dto;

import com.task.mgmt.constant.TaskStatus;
import com.task.mgmt.domain.TaskComment;
import com.task.mgmt.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskDTO extends AuditingDTO {

  private String title;
  private String description;
  public UserDTO assignee;
  private LocalDateTime dueDate;
  private TaskStatus status;
  private Set<TaskCommentDTO> comments = new HashSet<>();
}
