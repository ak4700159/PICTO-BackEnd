package picto.com.chattingscheduler.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.chattingscheduler.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
