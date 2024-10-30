package picto.com.generator.global.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.global.models.TitleList;

public interface TitleListRepository extends JpaRepository<TitleList, Integer> {
}
