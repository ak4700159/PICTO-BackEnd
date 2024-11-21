package picto.com.sessionscheduler.domain.session.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.sessionscheduler.domain.session.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
