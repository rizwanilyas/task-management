package com.task.mgmt.constant;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatus {
  TODO(1, "TODO"),
  IN_PROCESS(2, "In Process"),
  COMPLETED(3, "Completed");

  final int id;
  @JsonProperty
  final String title;
}
