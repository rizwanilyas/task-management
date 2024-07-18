package com.task.mgmt.mapper;

import com.task.mgmt.domain.User;
import com.task.mgmt.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {})
public interface UserMapper extends BaseMapper<UserDTO, User> {

}
