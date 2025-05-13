package picto.com.generator.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.domain.user.entity.Title;

public interface TitleRepository extends JpaRepository<Title, String> {
}
