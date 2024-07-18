package com.task.mgmt.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO extends AuditingDTO {

  private String email;

  private String firstName;

  private String lastName;

  private String image;

  private String username;

  private Boolean disabled;

  private Set<RoleDTO> roles = new HashSet<>();
}
