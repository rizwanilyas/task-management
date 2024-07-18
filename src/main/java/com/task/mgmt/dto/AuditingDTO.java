package com.task.mgmt.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuditingDTO {

  private Long id;

  private Instant createdDate;

  private Long createdBy;

  private Instant updatedDate;
  private Long updatedBy;
}
