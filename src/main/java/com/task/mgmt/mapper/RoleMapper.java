package com.task.mgmt.mapper;

import com.task.mgmt.domain.Role;
import com.task.mgmt.dto.RoleDTO;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {})
public interface RoleMapper extends BaseMapper<RoleDTO, Role> {

  RoleDTO toDto(Role d);
}
