package picto.com.generator.global.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.domain.user.domain.User;
import picto.com.generator.global.models.Filter;

public interface FilterRepositroy extends JpaRepository<Filter, Integer> {
}