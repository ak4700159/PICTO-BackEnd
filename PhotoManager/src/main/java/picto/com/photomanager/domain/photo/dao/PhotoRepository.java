package picto.com.photomanager.domain.photo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.domain.photo.entity.PhotoId;

import java.util.List;
@Repository
public interface PhotoRepository extends JpaRepository<Photo, PhotoId> {
    @Query("select p from Photo p where p.id.photoId = :photoId")
    List<Photo> findByPhoto(@Param("photoId") int photoId);

    @Query("select p from Photo p where p.id.userId = :userId")
    List<Photo> findByUser(@Param("userId") int photoId);

    //    @Query(value =
//            "SELECT p, " +
//                    "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.lat)) * " +
//                    "cos(radians(p.lng) - radians(:longitude)) + " +
//                    "sin(radians(:latitude)) * sin(radians(p.lat)))) AS distance " +
//                    "FROM Photo p " +
//                    "WHERE p.lat BETWEEN :latitude - (10/111.32) " +
//                    "AND :latitude + (10/111.32) " +
//                    "AND p.lng BETWEEN :longitude - (10/(111.32 * COS(RADIANS(:latitude)))) " +
//                    "AND :longitude + (10/(111.32 * COS(RADIANS(:latitude)))) " +
//                    "HAVING distance <= 10 ")
    @Query("select p from Photo p where p.lat = :latitude and p.lng = :longitude ")
    List<Photo> findByLocationInfo(@Param("latitude") double latitude, @Param("longitude") double longitude);



    @Query("select p from Photo p where p.location like %:location order by p.likes limit :count")
    List<Photo> findByTopPhoto(@Param("location") String location, @Param("count") int count);

    @Query("select p from Photo p where p.location like %:location order by RAND() limit :count ")
    List<Photo> findByRandomPhoto(@Param("location") String location, @Param("count") int count);
}
