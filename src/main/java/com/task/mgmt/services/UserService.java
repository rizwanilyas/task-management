package com.task.mgmt.services;

import static com.task.mgmt.constant.Constant.FORGET_PASSWORD_TEMPLATE;
import static com.task.mgmt.constant.Constant.NEW_ACCOUNT_TEMPLATE;

import com.task.mgmt.constant.ErrorCode;
import com.task.mgmt.domain.Role;
import com.task.mgmt.domain.Token;
import com.task.mgmt.domain.User;
import com.task.mgmt.dto.UserDTO;
import com.task.mgmt.dto.request.SignupUserRequest;
import com.task.mgmt.dto.request.ResetPasswordDto;
import com.task.mgmt.dto.request.UpdatePasswordRequest;
import com.task.mgmt.dto.request.UpdateUserRequest;
import com.task.mgmt.dto.response.MessageResponse;
import com.task.mgmt.exception.BadRequestException;
import com.task.mgmt.exception.RecordNotFoundException;
import com.task.mgmt.mapper.UserMapper;
import com.task.mgmt.repos.SessionRepository;
import com.task.mgmt.repos.TokenRepository;
import com.task.mgmt.repos.UserRepository;
import com.task.mgmt.utils.SearchSpecification;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@Data
public class UserService {

  private final UserRepository userRepository;
  private final RoleService roleService;
  private final TokenRepository tokenRepository;
  private final SessionRepository sessionRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final SpringTemplateEngine templateEngine;
  private final EmailService emailService;
  private final SearchSpecification<User> searchSpecification;

  @Value("${app.email.forget-password.subject}")
  private String forgetPasswordSubject;

  @Value("${app.email.new-account.subject}")
  private String newAccountSubject;

  @Value("${app.url.reset-password}")
  private String resetPasswordURL;

  public UserDTO findById(Long id) {
    return userMapper.toDto(getById(id));
  }

  public User getById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new RecordNotFoundException(ErrorCode.USER_NOT_FOUND));
  }

  public Page<UserDTO> list(Pageable pageable, Map<String, String> criteria) {
    Specification<User> specification = searchSpecification.getSpecification(criteria);
    return userRepository.findAll(specification, pageable).map(userMapper::toDto);
  }

  public List<UserDTO> list() {
    return userRepository.findAll().stream().map(userMapper::toDto).toList();
  }

  public User findUserByEmailOrUsername(String emailOrUserName) {
    return userRepository
        .findUserByEmailOrUsername(emailOrUserName)
        .orElseThrow(() -> new RecordNotFoundException(ErrorCode.USER_NOT_FOUND));
  }

  public void activate(Long userId) {
    User user = getById(userId);
    user.setDisabled(false);
    userRepository.save(user);
  }

  @Transactional
  public void deactivate(Long userId) {
    User user = getById(userId);
    user.setDisabled(true);
    userRepository.save(user);
    revokeAllSessions(user);
  }

  @Transactional
  public UserDTO update(Long id, UpdateUserRequest request) {
    User user = getById(id);
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    Set<Role> roles = roleService.findAllByIdIn(request.getRoles());
    user.setRoles(roles);
    userRepository.save(user);
    return userMapper.toDto(user);
  }

  public UserDTO updatePassword(Long loggedInUserId, UpdatePasswordRequest request) {
    User user = getById(loggedInUserId);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    userRepository.save(user);
    return userMapper.toDto(user);
  }

  public UserDTO create(@Valid SignupUserRequest request) {
    if (userRepository.existsByUsernameOrEmail(request.getUsername(), request.getEmail())) {
      throw new BadRequestException(ErrorCode.EMAIL_OR_USERNAME_ALREADY_EXIST);
    }
    Role role = roleService.findRegularUserRole();
    User user =
        User.builder()
            .email(request.getEmail())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .username(request.getUsername())
            .disabled(false)
            .password(passwordEncoder.encode(request.getPassword()))
            .roles(Set.of(role))
            .build();
    userRepository.save(user);
    return userMapper.toDto(user);
  }

  public MessageResponse sendPasswordReset(Long id) {
    User user = getById(id);
    return generateTokenAndSendEmail(user, FORGET_PASSWORD_TEMPLATE, forgetPasswordSubject);
  }

  public MessageResponse forgetPassword(String email) {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new RecordNotFoundException(ErrorCode.USER_NOT_FOUND));

    return generateTokenAndSendEmail(user, FORGET_PASSWORD_TEMPLATE, forgetPasswordSubject);
  }

  private void revokeAllSessions(User user) {
    var validUserTokens = sessionRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty()) {
      return;
    }
    validUserTokens.forEach(session -> session.setRevoked(true));
    sessionRepository.saveAll(validUserTokens);
  }

  private MessageResponse generateTokenAndSendEmail(User user, String template, String subject) {
    Instant expiredTime = Instant.now().plus(2, ChronoUnit.HOURS);
    String token = UUID.randomUUID().toString();
    Token token1 = new Token();
    token1.setToken(token);
    token1.setUser(user);
    token1.setExpireAt(expiredTime);
    tokenRepository.save(token1);

    Map<String, Object> templateVariables =
        Map.ofEntries(
            Map.entry("name", user.getUsername()),
            Map.entry("url", resetPasswordURL + "?token=" + token));

    var templateContext = new Context();
    templateContext.setVariables(templateVariables);
    var htmlBody = templateEngine.process(template, templateContext);
    emailService.sendEmail(user.getEmail(), subject, htmlBody);
    return new MessageResponse("password reset link emailed");
  }

  public MessageResponse resetPassword(ResetPasswordDto passwordResetDto) {
    Token userToken =
        tokenRepository
            .findByToken(passwordResetDto.getToken())
            .orElseThrow(() -> new RecordNotFoundException(ErrorCode.INVALID_TOKEN));
    User user =
        userRepository
            .findById(userToken.user.getId())
            .orElseThrow(() -> new RecordNotFoundException(ErrorCode.USER_NOT_FOUND));
    user.setPassword(passwordEncoder.encode(passwordResetDto.getPassword()));
    userRepository.save(user);
    return new MessageResponse("password updated successfully");
  }
}
