package com.task.mgmt.web;

import com.task.mgmt.dto.TaskCommentDTO;
import com.task.mgmt.dto.TaskDTO;
import com.task.mgmt.dto.request.CommentRequest;
import com.task.mgmt.dto.request.TaskRequest;
import com.task.mgmt.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Tasks", description = "Task management controller")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/tasks")
public class TaskController {

  private final TaskService taskService;

  @Operation(summary = "create")
  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REGULAR')")
  public ResponseEntity<TaskDTO> create(@RequestBody @Valid TaskRequest data) {
    final var response = taskService.create(data);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "update task details")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REGULAR')")
  @PutMapping("/{id}")
  public ResponseEntity<TaskDTO> update(
      @PathVariable Long id, @Valid @RequestBody TaskRequest data) {
    final var response = taskService.update(id, data);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get by Id")
  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REGULAR')")
  public ResponseEntity<TaskDTO> findById(@PathVariable("id") Long id) {
    final var response = taskService.findById(id);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Delete task by Id")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
    taskService.deleteTask(id);
    return ResponseEntity.accepted().build();
  }

  @Operation(summary = "Mark Task as completed")
  @PostMapping("/{id}/completed")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REGULAR')")
  public ResponseEntity<Void> completed(@PathVariable("id") Long id) {
    taskService.completed(id);
    return ResponseEntity.accepted().build();
  }

  @Operation(summary = "Mark Task as started")
  @PostMapping("/{id}/started")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REGULAR')")
  public ResponseEntity<Void> started(@PathVariable("id") Long id) {
    taskService.started(id);
    return ResponseEntity.accepted().build();
  }

  @Operation(summary = "Mark Task as todo")
  @PostMapping("/{id}/todo")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REGULAR')")
  public ResponseEntity<Void> todo(@PathVariable("id") Long id) {
    taskService.todo(id);
    return ResponseEntity.accepted().build();
  }

  @Operation(summary = "Update task assignee")
  @PostMapping("/{id}/assignee/{assigneeId}")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REGULAR')")
  public ResponseEntity<Void> updateAssignee(
      @PathVariable("id") Long id, @PathVariable("assigneeId") Long assigneeId) {
    taskService.updateAssignee(id, assigneeId);
    return ResponseEntity.accepted().build();
  }

  @Operation(summary = "Add comment on task")
  @PostMapping("/{id}/comment")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REGULAR')")
  public ResponseEntity<TaskCommentDTO> addComment(
      @PathVariable("id") Long taskId, @Valid @RequestBody CommentRequest commentRequest) {
    final var response = taskService.addComment(taskId, commentRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "update task comment")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REGULAR')")
  @PutMapping("/{id}/comment/{commentId}")
  public ResponseEntity<TaskCommentDTO> updateComment(
      @PathVariable(name = "id") Long taskId,
      @PathVariable(name = "commentId") Long commentId,
      @Valid @RequestBody CommentRequest request) {
    final var response = taskService.updateComment(taskId, commentId, request);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "delete comment")
  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping("/{id}/comment/{commentId}")
  public ResponseEntity<Void> deleteComment(
      @PathVariable(name = "id") Long taskId, @PathVariable(name = "commentId") Long commentId) {
    taskService.deleteComment(taskId, commentId);
    return ResponseEntity.accepted().build();
  }

  @Operation(summary = "List")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REGULAR')")
  @GetMapping
  public Page<TaskDTO> list(Pageable pageable, @RequestParam(required = false) TaskSearchCriteria criteria) {
    return taskService.list(pageable, criteria);
  }
}
