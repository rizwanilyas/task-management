package com.task.mgmt.web;

import com.task.mgmt.dto.request.SignupUserRequest;
import com.task.mgmt.dto.request.LoginRequest;
import com.task.mgmt.dto.request.ResetPasswordDto;
import com.task.mgmt.dto.response.AuthenticationResponse;
import com.task.mgmt.dto.response.MessageResponse;
import com.task.mgmt.services.AuthenticationService;
import com.task.mgmt.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "auth", description = "auth related operations")
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationService service;
  private final UserService userService;

  @CrossOrigin
  @Operation(summary = "login")
  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(service.login(request));
  }

  @Operation(summary = "signup")
  @PostMapping("/signup")
  public ResponseEntity<Void> register(@RequestBody @Valid SignupUserRequest data) {
    userService.create(data);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "refresh token")
  @PostMapping("/refresh-token")
  public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    service.refreshToken(request, response);
    return ResponseEntity.accepted().build();
  }

  @Operation(summary = "forget password")
  @PostMapping("/forget-password/{email}")
  public ResponseEntity<MessageResponse> forgetPassword(@PathVariable String email) {
    final var response = service.forgetPassword(email);
    return ResponseEntity.accepted().body(response);
  }

  @Operation(summary = "reset password")
  @PostMapping("/reset-password")
  public ResponseEntity<MessageResponse> resetPassword(
      @RequestBody ResetPasswordDto passwordChangeDTO) {
    final var response = service.resetPassword(passwordChangeDTO);
    return ResponseEntity.accepted().body(response);
  }
}
