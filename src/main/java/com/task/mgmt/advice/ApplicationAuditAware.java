package com.task.mgmt.advice;

import com.task.mgmt.security.UserDetailsImpl;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ApplicationAuditAware implements AuditorAware<Long> {

  // just code changes
  @Override
  public Optional<Long> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
            || !authentication.isAuthenticated()
            || authentication instanceof AnonymousAuthenticationToken) {
      return Optional.empty();
    }
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    return Optional.ofNullable(userPrincipal.getId());
  }
}
