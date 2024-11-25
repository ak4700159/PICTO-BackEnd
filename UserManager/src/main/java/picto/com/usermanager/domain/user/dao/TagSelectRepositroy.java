package picto.com.usermanager.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import picto.com.usermanager.domain.user.entity.TagSelect;
import picto.com.usermanager.domain.user.entity.TagSelectId;

import java.util.List;

public interface TagSelectRepositroy extends JpaRepository<TagSelect, TagSelectId> {
    @Query("select tag from TagSelect tag where tag.tagSelectedId.userId = :userId")
    public List<TagSelect> findByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("delete from TagSelect tag where tag.tagSelectedId.userId = :userId")
    public void deleteByUserId(@Param("userId") Long userId);
}
