package com.task.mgmt.security;

import com.task.mgmt.domain.User;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private static final Long DEFAULT_USER = -1L;

  public Long getLoggedInUserId() {
    User loggedInUser = getLoggedInUser();
    if (loggedInUser == null) {
      return DEFAULT_USER;
    }
    return loggedInUser.getId();
  }

  public User getLoggedInUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null
        || auth.getPrincipal() == null
        || !(auth.getPrincipal() instanceof UserDetailsImpl usr)) {
      return null;
    }
    return new User(usr.getId());
  }
}
