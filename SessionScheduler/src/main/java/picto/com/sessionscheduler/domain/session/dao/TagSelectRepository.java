package picto.com.sessionscheduler.domain.session.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import picto.com.sessionscheduler.domain.session.entity.TagSelect;
import picto.com.sessionscheduler.domain.session.entity.TagSelectId;


import java.util.List;

public interface TagSelectRepository extends JpaRepository<TagSelect, TagSelectId> {
    @Query("SELECT t FROM TagSelect t WHERE t.id.userId = :userId")
    List<TagSelect> findByUserId(@Param("userId") Long userId);
}
