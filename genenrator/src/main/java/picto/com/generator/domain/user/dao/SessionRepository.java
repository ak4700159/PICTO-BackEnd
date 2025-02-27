package picto.com.generator.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.domain.user.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
