package picto.com.photomanager.global.getDomain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.photomanager.global.getDomain.entity.Filter;

public interface FilterRepository extends JpaRepository<Filter, Long> {
}
