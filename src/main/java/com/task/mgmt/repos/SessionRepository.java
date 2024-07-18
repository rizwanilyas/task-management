package com.task.mgmt.repos;

import com.task.mgmt.domain.Session;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

  @Query(
      value =
          """
              select t from Session t inner join User u\s
              on t.user.id = u.id\s
              where u.id = :id and t.revoked = false\s
              """)
  List<Session> findAllValidTokenByUser(Long id);

  Optional<Session> findByAccessToken(String session);
}
