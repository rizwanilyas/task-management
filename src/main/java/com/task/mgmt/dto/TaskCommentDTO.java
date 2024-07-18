package com.task.mgmt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskCommentDTO extends AuditingDTO {

  private String comments;
}
