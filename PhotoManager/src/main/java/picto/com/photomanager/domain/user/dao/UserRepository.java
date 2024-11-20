package picto.com.photomanager.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.photomanager.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
