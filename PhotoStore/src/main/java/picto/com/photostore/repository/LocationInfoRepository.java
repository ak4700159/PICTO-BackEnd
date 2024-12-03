package picto.com.photostore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.photostore.domain.LocationInfo;

public interface LocationInfoRepository extends JpaRepository<LocationInfo, Long> {
}
