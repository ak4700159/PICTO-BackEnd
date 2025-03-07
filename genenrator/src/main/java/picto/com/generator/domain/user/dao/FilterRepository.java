package picto.com.generator.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.domain.user.entity.Filter;

public interface FilterRepository extends JpaRepository<Filter, Long> {
}
