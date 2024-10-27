package picto.com.generator.global.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.domain.user.domain.User;
import picto.com.generator.global.models.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {
}
