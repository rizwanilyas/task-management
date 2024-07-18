package com.task.mgmt.mapper;

import com.task.mgmt.domain.Task;
import com.task.mgmt.domain.TaskComment;
import com.task.mgmt.dto.TaskCommentDTO;
import com.task.mgmt.dto.TaskDTO;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {})
public interface TaskCommentMapper extends BaseMapper<TaskCommentDTO, TaskComment> {

}
