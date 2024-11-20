package picto.com.photomanager.global.getDomain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.photomanager.global.getDomain.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
