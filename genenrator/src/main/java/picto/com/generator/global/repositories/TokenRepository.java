package picto.com.generator.global.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.global.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {
}
