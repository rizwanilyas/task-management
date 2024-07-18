package com.task.mgmt.repos;

import com.task.mgmt.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

  @Query("select u from User u where u.email = :username or u.username = :username")
  Optional<User> findUserByEmailOrUsername(@Param("username") String username);

  Boolean existsByUsernameOrEmail(@Param("username") String username,
      @Param("email") String email);

  Optional<User> findByEmail(String email);
}
