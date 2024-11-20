package picto.com.generator.global.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.global.entity.Filter;

public interface FilterRepository extends JpaRepository<Filter, Long> {
}
