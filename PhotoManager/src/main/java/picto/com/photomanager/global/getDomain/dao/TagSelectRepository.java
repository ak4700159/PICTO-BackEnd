package picto.com.photomanager.global.getDomain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import picto.com.photomanager.global.getDomain.entity.TagSelect;
import picto.com.photomanager.global.getDomain.entity.TagSelectId;

import java.util.List;

public interface TagSelectRepository extends JpaRepository<TagSelect, TagSelectId> {
    @Query("SELECT t FROM TagSelect t WHERE t.user.userId = :userId")
    List<TagSelect> findByUserId(@Param("userId") int userId);
}
