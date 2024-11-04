package picto.com.photomanager.global.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.photomanager.global.user.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
