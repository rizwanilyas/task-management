package com.task.mgmt.web;

import com.task.mgmt.dto.UserDTO;
import com.task.mgmt.dto.request.SignupUserRequest;
import com.task.mgmt.dto.request.UpdatePasswordRequest;
import com.task.mgmt.dto.request.UpdateUserRequest;
import com.task.mgmt.dto.response.MessageResponse;
import com.task.mgmt.security.AuthService;
import com.task.mgmt.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "users", description = "user related operations")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserController {

  private final UserService userService;
  private final AuthService authService;

  @Operation(summary = "Get by Id")
  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<UserDTO> findById(@PathVariable("id") Long id) {
    final var response = userService.findById(id);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "create")
  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<UserDTO> create(@RequestBody @Valid SignupUserRequest data) {
    final var response = userService.create(data);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "activate")
  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("/{id}/activate")
  public ResponseEntity<Void> activate(@PathVariable Long id) {
    userService.activate(id);
    return ResponseEntity.accepted().build();
  }

  @Operation(summary = "deactivate")
  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("/{id}/deactivate")
  public ResponseEntity<Void> deactivate(@PathVariable Long id) {
    userService.deactivate(id);
    return ResponseEntity.accepted().build();
  }

  @Operation(summary = "update user roles & name")
  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> update(
      @PathVariable Long id, @Valid @RequestBody UpdateUserRequest data) {
    final var response = userService.update(id, data);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "change my password")
  @PutMapping("/update-password")
  public ResponseEntity<UserDTO> updatePassword(@RequestBody @Valid UpdatePasswordRequest data) {
    final var response = userService.updatePassword(authService.getLoggedInUserId(), data);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "send password reset link")
  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("/{id}/send-password-reset")
  public ResponseEntity<MessageResponse> sendPasswordReset(@PathVariable Long id) {
    final var response = userService.sendPasswordReset(id);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "List")
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping
  public Page<UserDTO> list(Pageable pageable, @RequestParam Map<String, String> criteria) {
    return userService.list(pageable, criteria);
  }

  @Operation(summary = "List All")
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/all")
  public List<UserDTO> list() {
    return userService.list();
  }

  @Operation(summary = "Get current user data")
  @GetMapping("/current-user")
  public ResponseEntity<UserDTO> currentUser() {
    final var response = userService.findById(authService.getLoggedInUserId());
    return ResponseEntity.ok(response);
  }
}
