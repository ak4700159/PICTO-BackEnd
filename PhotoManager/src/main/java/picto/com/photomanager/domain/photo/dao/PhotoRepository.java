package picto.com.photomanager.domain.photo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.domain.photo.entity.PhotoId;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, PhotoId> {
    @Query("select p from Photo p where p.id.photoID = :photoId")
    List<Photo> findByPhotoID(@Param("photoId") int photoId);

    @Query("select p from Photo p where p.id.userId = :userId")
    List<Photo> findByUserID(@Param("userId") int photoId);

    @Query(value =
            "SELECT p, " +
                    "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.lat)) * " +
                    "cos(radians(p.lng) - radians(:longitude)) + " +
                    "sin(radians(:latitude)) * sin(radians(p.lat)))) AS distance " +
                    "FROM Photo p " +
                    "WHERE p.lat BETWEEN :latitude - (10/111.32) " +
                    "AND :latitude + (10/111.32) " +
                    "AND p.lng BETWEEN :longitude - (10/(111.32 * COS(RADIANS(:latitude)))) " +
                    "AND :longitude + (10/(111.32 * COS(RADIANS(:latitude)))) " +
                    "HAVING distance <= 10 ")
    List<Photo> findByLocation(@Param("latitude") double latitude, @Param("longitude") double longitude);

}
