package picto.com.genenrator.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.genenrator.domain.user.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
