package picto.com.usermanager.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import picto.com.usermanager.domain.user.entity.Photo;
import picto.com.usermanager.domain.user.entity.PhotoId;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, PhotoId> {
    @Query("select p from Photo p where p.id.userId = :userId")
    public List<Photo> findByUserId(@Param("userId") Integer userId);
}
