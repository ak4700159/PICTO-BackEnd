package picto.com.usermanager.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.usermanager.domain.user.entity.TitleList;

public interface TitleListRepository extends JpaRepository<TitleList, Integer> {
}
