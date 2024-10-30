package picto.com.generator.global.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.global.models.Title;

public interface TitleRepository extends JpaRepository<Title, String> {
}
