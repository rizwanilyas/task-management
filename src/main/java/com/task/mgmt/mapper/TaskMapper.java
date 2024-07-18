package com.task.mgmt.mapper;

import com.task.mgmt.domain.Task;
import com.task.mgmt.dto.TaskDTO;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {})
public interface TaskMapper extends BaseMapper<TaskDTO, Task> {

}
