package com.task.mgmt.services;

import com.task.mgmt.constant.ErrorCode;
import com.task.mgmt.domain.Role;
import com.task.mgmt.dto.RoleDTO;
import com.task.mgmt.exception.RecordNotFoundException;
import com.task.mgmt.mapper.RoleMapper;
import com.task.mgmt.repos.RoleRepository;
import java.util.List;
import java.util.Set;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class RoleService {

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  enum RoleEnum {
    ADMIN,
    REGULAR
  }

  public List<RoleDTO> list() {
    return roleRepository.findAll().stream().map(roleMapper::toDto).toList();
  }

  public Role findRegularUserRole() {
    return roleRepository
        .findByCode(RoleEnum.REGULAR.name())
        .orElseThrow(() -> new RecordNotFoundException(ErrorCode.ROLE_NOT_FOUND));
  }

  public Set<Role> findAllByIdIn(Set<Long> roles) {
    return roleRepository.findAllByIdIn(roles);
  }
}
