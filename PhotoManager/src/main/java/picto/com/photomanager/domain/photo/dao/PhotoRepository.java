package picto.com.photomanager.domain.photo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import picto.com.photomanager.domain.photo.entity.Photo;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Query("select p from Photo p where p.photoId = :photoId")
    List<Photo> findByPhoto(@Param("photoId") Long photoId);

    @Query("select p from Photo p where p.userId = :userId")
    List<Photo> findByUser(@Param("userId") Long photoId);

    // 랜덤 사진 조회
    @Query(value = """
            select p.*
            from (
                select p2.*
                FROM Photo p2
                WHERE p2.shared_active = 1
                  AND EXISTS (
                      SELECT 1
                      FROM TagSelect ts
                      WHERE ts.user_id = :userId AND ts.tag = p2.tag)
            ) p
            where p.location like CONCAT('%', :location, '%') order by RAND() limit :count
            """, nativeQuery = true)
    List<Photo> findByRandomPhoto(@Param("location") String location, @Param("count") int count, @Param("userId") Long userId);

    // 반경 3km 이내의 사진 조회
    @Query(value = """
                SELECT p.*
                FROM Photo p
                WHERE ST_Distance_Sphere(
                    point(p.lng, p.lat),
                    point(:longitude, :latitude)
                ) <= 3000  -- 미터 단위
            """, nativeQuery = true)
    List<Photo> findByLocationInfo(@Param("latitude") double latitude, @Param("longitude") double longitude);

    // 지역별 대표 사진 조회
    @Query(value = """
            SELECT sub.photo_id, sub.user_id, sub.lat, sub.lng, sub.location,
                   sub.register_datetime, sub.upload_datetime,
                   sub.likes, sub.views, sub.tag, sub.shared_active,
                   sub.frame_active, sub.photo_path
            FROM (
                SELECT p.*, info.large_name,
                       ROW_NUMBER() OVER (
                           PARTITION BY info.large_name
                           ORDER BY p.likes DESC
                       ) AS ranking
                FROM picto_schema.Photo p
                JOIN picto_schema.LocationInfo info ON p.photo_id = info.photo_id
                WHERE p.shared_active = 1
                  AND EXISTS (
                      SELECT 1
                      FROM picto_schema.TagSelect ts
                      WHERE ts.user_id = :userId
                        AND ts.tag = p.tag
                  )
            ) sub
            WHERE sub.ranking <= :count
            """, nativeQuery = true)
    List<Photo> findByTypeTopLargePhoto(
            @Param("userId") Long userId,
            @Param("count") int count
    );

    @Query(value = """
            SELECT sub.photo_id,sub.user_id, sub.lat, sub.lng, sub.location, sub.register_datetime, sub.upload_datetime, sub.likes, sub.views, sub.tag, sub.shared_active, sub.frame_active, sub.photo_path
            FROM (
                SELECT p.*, info.middle_name,
                       ROW_NUMBER() OVER (
                           PARTITION BY info.middle_name
                           ORDER BY p.likes DESC
                       ) AS ranking
                FROM picto_schema.Photo p
                JOIN picto_schema.LocationInfo info ON p.photo_id = info.photo_id
                WHERE p.shared_active = 1
                  AND EXISTS (
                      SELECT 1
                      FROM picto_schema.TagSelect ts
                      WHERE ts.user_id = :userId
                        AND ts.tag = p.tag
                  )
            ) sub
            WHERE sub.ranking <= :count
            """, nativeQuery = true)
    List<Photo> findByTypeTopMiddlePhoto(@Param("userId") Long userId, @Param("count") int count);

    @Query(value = """
            SELECT sub.photo_id,sub.user_id, sub.lat, sub.lng, sub.location, sub.register_datetime, sub.upload_datetime, sub.likes, sub.views, sub.tag, sub.shared_active, sub.frame_active, sub.photo_path
            FROM (
                SELECT p.*, info.small_name,
                       ROW_NUMBER() OVER (
                           PARTITION BY info.small_name
                           ORDER BY p.likes DESC
                       ) AS ranking
                FROM picto_schema.Photo p
                JOIN picto_schema.LocationInfo info ON p.photo_id = info.photo_id
                WHERE p.shared_active = 1
                  AND EXISTS (
                      SELECT 1
                      FROM picto_schema.TagSelect ts
                      WHERE ts.user_id = :userId
                        AND ts.tag = p.tag
                  )
            ) sub
            WHERE sub.ranking <= :count
            """, nativeQuery = true)
    List<Photo> findByTypeTopSmallPhoto(@Param("userId") Long userId, @Param("count") int count);

    // 특정 지역에 대한 대표사진들 조회
    @Query("select p " +
            "from Photo p join LocationInfo info on p.photoId = info.photo.photoId " +
            "where info.smallName = :locationName and p.sharedActive = true " +
            "order by p.likes desc " +
            "limit :count")
    List<Photo> findByNameSmallPhoto(@Param("locationName") String locationName, @Param("count") int count);

    @Query("select p " +
            "from Photo p join LocationInfo info on p.photoId = info.photo.photoId " +
            "where info.middleName = :locationName and p.sharedActive = true " +
            "order by p.likes desc " +
            "limit :count")
    List<Photo> findByNameMiddlePhoto(@Param("locationName") String locationName, @Param("count") int count);

    @Query("select p " +
            "from Photo p join LocationInfo info on p.photoId = info.photo.photoId " +
            "where info.largeName = :locationName and p.sharedActive = true " +
            "order by p.likes desc " +
            "limit :count")
    List<Photo> findByNameLargePhoto(@Param("locationName") String locationName, @Param("count") int count);

    @Query("select p " +
            "from Photo p " +
            "where p.sharedActive = true " +
            "order by p.likes desc ")
    List<Photo> findSharedActivePhotos();
}
