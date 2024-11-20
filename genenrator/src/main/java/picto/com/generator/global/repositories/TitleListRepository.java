package picto.com.generator.global.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.global.entity.TitleList;

public interface TitleListRepository extends JpaRepository<TitleList, Long> {
}
