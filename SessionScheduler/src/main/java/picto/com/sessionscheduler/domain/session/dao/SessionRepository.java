package picto.com.sessionscheduler.domain.session.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.sessionscheduler.domain.session.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
