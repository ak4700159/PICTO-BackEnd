package picto.com.usermanager.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.usermanager.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByEmail(String email);

    User getUserByAccountName(String accountName);
}
