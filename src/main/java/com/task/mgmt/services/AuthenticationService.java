package com.task.mgmt.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.mgmt.domain.Session;
import com.task.mgmt.domain.User;
import com.task.mgmt.dto.request.LoginRequest;
import com.task.mgmt.dto.request.ResetPasswordDto;
import com.task.mgmt.dto.response.AuthenticationResponse;
import com.task.mgmt.dto.response.MessageResponse;
import com.task.mgmt.repos.RoleRepository;
import com.task.mgmt.repos.SessionRepository;
import com.task.mgmt.security.JwtService;
import com.task.mgmt.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserService userService;

  private final RoleRepository roleRepository;
  private final SessionRepository sessionRepository;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Value("${app.security.jwt.expiration}")
  private Long expiryDuration;

  public AuthenticationResponse login(LoginRequest request) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    var user = userService.findUserByEmailOrUsername(request.getUsername());

    var jwtToken = jwtService.generateToken(userDetails);
    var refreshToken = jwtService.generateRefreshToken(userDetails);
    saveUserSession(user, jwtToken, refreshToken);

    List<String> features =
        userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .id(user.getId())
        .email(user.getEmail())
        .username(user.getUsername())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .authorities(features)
        .build();
  }

  public void refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.userService.findUserByEmailOrUsername(userEmail);

      UserDetailsImpl details = UserDetailsImpl.build(user);
      if (jwtService.isTokenValid(refreshToken, details)) {
        var accessToken = jwtService.generateToken(details);
        saveUserSession(user, accessToken, refreshToken);

        List<String> features =
            details.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        var authResponse =
            AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .authorities(features)
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

  public MessageResponse forgetPassword(String email) {
    return userService.forgetPassword(email);
  }

  private void saveUserSession(User user, String jwtToken, String refreshToken) {
    var token =
        Session.builder()
            .user(user)
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .expiredAt(Instant.now().plus(expiryDuration, ChronoUnit.MILLIS))
            .revoked(false)
            .build();
    sessionRepository.save(token);
  }

  public MessageResponse resetPassword(ResetPasswordDto passwordResetDto) {
    return userService.resetPassword(passwordResetDto);
  }
}
