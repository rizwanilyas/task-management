package com.task.mgmt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.task.mgmt.constant.TaskStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task extends AuditingEntity {

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
  @JoinColumn(name = "assignee_id", referencedColumnName = "id")
  public User assignee;

  @Column(name = "due_date")
  private LocalDateTime dueDate;

  @Column(name = "status")
  private TaskStatus status;

  @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonIgnoreProperties(
      value = {"task"},
      allowSetters = true)
  private Set<TaskComment> comments = new HashSet<>();
}
