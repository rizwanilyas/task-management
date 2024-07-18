package com.task.mgmt.repos;

import com.task.mgmt.domain.Token;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

  Optional<Token> findByToken(String token);
}
