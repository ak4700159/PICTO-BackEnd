package picto.com.usermanager.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.usermanager.domain.user.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
