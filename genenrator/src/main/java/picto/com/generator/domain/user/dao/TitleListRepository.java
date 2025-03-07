package picto.com.generator.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.domain.user.entity.TitleList;

public interface TitleListRepository extends JpaRepository<TitleList, Long> {
}
