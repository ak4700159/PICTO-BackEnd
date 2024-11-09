package picto.com.generator.global.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.global.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Integer> {
}
