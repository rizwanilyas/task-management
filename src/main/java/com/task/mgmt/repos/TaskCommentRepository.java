package com.task.mgmt.repos;

import com.task.mgmt.domain.TaskComment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment, Long>, JpaSpecificationExecutor<TaskComment> {

  Optional<TaskComment> findByIdAndTaskId(Long id, Long taskId);
}
