package com.task.mgmt.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.mgmt.constant.Constant;
import com.task.mgmt.dto.response.ExceptionResponse;
import com.task.mgmt.repos.SessionRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final SessionRepository sessionRepository;

  private final ObjectMapper mapper;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    final String authHeader = request.getHeader(Constant.AUTHORIZATION);
    final String jwt;
    final String userEmail;

    if (authHeader == null || !authHeader.startsWith(Constant.BEARER)) {
      filterChain.doFilter(request, response);
      return;
    }
    jwt = authHeader.substring(Constant.BEARER.length());
    try {
      userEmail = jwtService.extractUsername(jwt);
      if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        var isTokenValid =
            sessionRepository.findByAccessToken(jwt).map(t -> !t.isRevoked()).orElse(false);

        if (isTokenValid && jwtService.isTokenValid(jwt, userDetails)) {
          UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
          ExceptionResponse exceptionResponse =
              new ExceptionResponse(
                  HttpStatus.FORBIDDEN.getReasonPhrase(),
                  HttpStatus.FORBIDDEN.getReasonPhrase(),
                  HttpStatus.FORBIDDEN,
                  LocalDateTime.now());

          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
          mapper.writeValue(response.getOutputStream(), exceptionResponse);
        }
      }
    } catch (Exception ex) {
      new ObjectMapper().writeValue(response.getOutputStream(), ex.getMessage());
    }
    filterChain.doFilter(request, response);
  }
}
