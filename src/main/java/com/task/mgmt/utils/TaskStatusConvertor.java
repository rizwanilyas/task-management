package com.task.mgmt.utils;

import com.task.mgmt.constant.TaskStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class TaskStatusConvertor implements AttributeConverter<TaskStatus, Integer> {

  @Override
  public Integer convertToDatabaseColumn(TaskStatus transactionType) {
    if (transactionType == null) {
      return null;
    }
    return transactionType.getId();
  }

  @Override
  public TaskStatus convertToEntityAttribute(Integer integer) {
    if (integer == null || integer == 0) {
      return null;
    }

    return Stream.of(TaskStatus.values())
        .filter(c -> c.getId() == integer)
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
