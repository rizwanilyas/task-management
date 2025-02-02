package com.task.mgmt.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDTO  {

  private Long id;
  private String name;
  private String code;
  private String description;
}
