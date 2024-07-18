package com.task.mgmt.security;

import static com.task.mgmt.constant.Constant.AUTHORIZATION;
import static com.task.mgmt.constant.Constant.BEARER;

import com.task.mgmt.repos.SessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

  private final SessionRepository sessionRepository;

  @Override
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    final String authHeader = request.getHeader(AUTHORIZATION);
    final String jwt;
    if (authHeader == null || !authHeader.startsWith(BEARER)) {
      return;
    }
    jwt = authHeader.substring(BEARER.length());
    var storedToken = sessionRepository.findByAccessToken(jwt).orElse(null);
    if (storedToken != null) {
      storedToken.setRevoked(true);
      sessionRepository.save(storedToken);
      SecurityContextHolder.clearContext();
    }
  }
}
