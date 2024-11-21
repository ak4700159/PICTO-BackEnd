package picto.com.sessionscheduler.domain.session.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import picto.com.sessionscheduler.domain.session.entity.Photo;
import picto.com.sessionscheduler.domain.session.entity.PhotoId;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, PhotoId> {
    @Query(value = "SELECT p FROM Photo p " +
            "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(p.lat)) * " +
            "cos(radians(:longitude) - radians(p.lng)) + " +
            "sin(radians(:latitude)) * sin(radians(p.lat)))) <= 5")
    List<Photo> findByLocationInfo(@Param("latitude") double latitude, @Param("longitude") double longitude);
}
