package picto.com.photomanager.global.getDomain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import picto.com.photomanager.global.getDomain.entity.Mark;
import picto.com.photomanager.global.getDomain.entity.MarkId;


import java.util.List;

public interface MarkRepository extends JpaRepository<Mark, MarkId> {
    @Query("select m from Mark m where m.marking.userId = :userId")
    List<Mark> findByUserId(@Param("userId") Long userId);
}
