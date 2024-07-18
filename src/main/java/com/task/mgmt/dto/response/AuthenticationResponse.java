package com.task.mgmt.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AuthenticationResponse {

  @JsonProperty("token")
  private String accessToken;

  @JsonProperty("refreshToken")
  private String refreshToken;

  private Long id;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private List<String> authorities;
}
