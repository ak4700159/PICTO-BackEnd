package picto.com.generator.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.domain.user.entity.LocationInfo;

public interface LocationInfoRepository extends JpaRepository<LocationInfo, Long> {
}
