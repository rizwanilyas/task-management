package com.task.mgmt.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sessions")
public class Session extends AuditingEntity {

  @Column(name = "expired_at")
  public Instant expiredAt;

  @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  public User user;

  @Column(unique = true, name = "access_token")
  private String accessToken;

  @Column(unique = true, name = "refresh_token")
  private String refreshToken;

  @Column(name = "revoked", columnDefinition = "boolean not null default false")
  private boolean revoked;
}
