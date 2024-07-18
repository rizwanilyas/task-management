package com.task.mgmt.web;

import com.task.mgmt.constant.TaskStatus;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public interface TaskSearchCriteria {

  List<TaskStatus> getStatus();

  String getSearchString();

  @DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm")
  Date getStartDateTime();

  @DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm")
  Date getEndDateTime();
}
