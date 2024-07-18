package com.task.mgmt.repos;

import com.task.mgmt.domain.Role;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByCode(String key);

    Set<Role> findAllByIdIn(Set<Long> roles);
}
