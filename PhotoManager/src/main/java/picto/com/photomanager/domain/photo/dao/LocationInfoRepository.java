package picto.com.photomanager.domain.photo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.photomanager.domain.photo.entity.LocationInfo;
import picto.com.photomanager.domain.photo.entity.LocationInfoId;

public interface LocationInfoRepository extends JpaRepository<LocationInfo, LocationInfoId> {
}