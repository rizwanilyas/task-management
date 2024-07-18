package com.task.mgmt.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task_comments")
public class TaskComment extends AuditingEntity {

  @Column(name = "comments")
  private String comments;

  @ManyToOne(fetch = FetchType.LAZY)
  private Task task;
}
