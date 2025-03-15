package picto.com.photostore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.photostore.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}