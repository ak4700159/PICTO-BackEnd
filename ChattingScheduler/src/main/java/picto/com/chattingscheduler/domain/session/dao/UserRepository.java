package picto.com.chattingscheduler.domain.session.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.chattingscheduler.domain.session.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
