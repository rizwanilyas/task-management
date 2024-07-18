package com.task.mgmt.services;

import com.task.mgmt.constant.ErrorCode;
import com.task.mgmt.constant.TaskStatus;
import com.task.mgmt.domain.Task;
import com.task.mgmt.domain.TaskComment;
import com.task.mgmt.domain.User;
import com.task.mgmt.dto.TaskCommentDTO;
import com.task.mgmt.dto.TaskDTO;
import com.task.mgmt.dto.request.CommentRequest;
import com.task.mgmt.dto.request.TaskRequest;
import com.task.mgmt.exception.RecordNotFoundException;
import com.task.mgmt.mapper.TaskCommentMapper;
import com.task.mgmt.mapper.TaskMapper;
import com.task.mgmt.repos.TaskCommentRepository;
import com.task.mgmt.repos.TaskRepository;
import com.task.mgmt.web.TaskSearchCriteria;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Data
public class TaskService {

  private final TaskRepository taskRepository;
  private final TaskCommentRepository taskCommentRepository;
  private final UserService userService;
  private final TaskMapper taskMapper;
  private final TaskCommentMapper taskCommentMapper;

  /************************************************
   * TASKS
   *************************************************/
  public TaskDTO create(@Valid TaskRequest request) {
    User assignee = null;
    if (request.getAssigneeId() != null && request.getAssigneeId() != 0) {
      assignee = userService.getById(request.getAssigneeId());
    }
    Task pc =
        Task.builder()
            .status(TaskStatus.TODO)
            .title(request.getTitle())
            .description(request.getDescription())
            .dueDate(request.getDueDate())
            .assignee(assignee)
            .build();
    taskRepository.save(pc);
    return taskMapper.toDto(pc);
  }

  public Page<TaskDTO> list(Pageable pageable, TaskSearchCriteria criteria) {
    Specification<Task> specification = getSpecification(criteria);
    return taskRepository.findAll(specification, pageable).map(taskMapper::toDto);
  }

  public TaskDTO update(Long taskId, @Valid TaskRequest request) {
    Task task = getById(taskId);
    task.setTitle(request.getTitle());
    task.setDescription(request.getDescription());
    task.setDueDate(request.getDueDate());
    taskRepository.save(task);
    return taskMapper.toDto(task);
  }

  public TaskDTO findById(Long id) {
    return taskMapper.toDto(getById(id));
  }

  public void deleteTask(Long taskId) {
    final var task = getById(taskId);
    taskRepository.delete(task);
  }

  public void updateAssignee(Long taskId, Long assigneeId) {
    Task task = getById(taskId);
    User assignee = userService.getById(assigneeId);
    task.setAssignee(assignee);
    taskRepository.save(task);
  }

  public void todo(Long taskId) {
    Task task = getById(taskId);
    task.setStatus(TaskStatus.TODO);
    taskRepository.save(task);
  }

  public void started(Long taskId) {
    Task task = getById(taskId);
    task.setStatus(TaskStatus.IN_PROCESS);
    taskRepository.save(task);
  }

  public void completed(Long taskId) {
    Task task = getById(taskId);
    task.setStatus(TaskStatus.COMPLETED);
    taskRepository.save(task);
  }

  /************************************************
   * COMMENTS
   *************************************************/
  public TaskCommentDTO addComment(Long taskId, @Valid CommentRequest request) {
    Task task = getById(taskId);
    TaskComment taskComment =
        TaskComment.builder().comments(request.getComments()).task(task).build();
    TaskComment savedComment = taskCommentRepository.save(taskComment);
    return taskCommentMapper.toDto(savedComment);
  }

  public TaskCommentDTO updateComment(Long taskId, Long commentId, @Valid CommentRequest request) {
    final var comment = getCommentById(commentId, taskId);
    comment.setComments(request.getComments());
    TaskComment savedComment = taskCommentRepository.save(comment);
    return taskCommentMapper.toDto(savedComment);
  }

  public void deleteComment(Long commentId, Long taskId) {
    final var comment = getCommentById(commentId, taskId);
    taskCommentRepository.delete(comment);
  }

  private TaskComment getCommentById(Long id, Long taskId) {
    return taskCommentRepository
        .findByIdAndTaskId(id, taskId)
        .orElseThrow(() -> new RecordNotFoundException(ErrorCode.TASK_COMMENT_NOT_FOUND));
  }

  private Task getById(Long id) {
    return taskRepository
        .findById(id)
        .orElseThrow(() -> new RecordNotFoundException(ErrorCode.TASK_NOT_FOUND));
  }

  /**
   search by status, due date range, title & description
   */
  public Specification<Task> getSpecification(TaskSearchCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if(Objects.isNull(criteria)) return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

      if (Objects.nonNull(criteria.getStatus())) {
        predicates.add(criteriaBuilder.in(root.get("status")).value(criteria.getStatus()));
      }
      if (Objects.nonNull(criteria.getStartDateTime())
          && Objects.nonNull(criteria.getEndDateTime())) {
        predicates.add(
            criteriaBuilder.between(
                root.get("dueDate"), criteria.getStartDateTime(), criteria.getEndDateTime()));
      }
      if (Objects.nonNull(criteria.getSearchString())) {
        Predicate predicateForData =
            criteriaBuilder.or(
                criteriaBuilder.like(root.get("title"), "%" + criteria.getSearchString() + "%"),
                criteriaBuilder.like(
                    root.get("description"), "%" + criteria.getSearchString() + "%"));
        predicates.add(predicateForData);
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
