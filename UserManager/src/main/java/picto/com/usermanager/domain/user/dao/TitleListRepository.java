package picto.com.usermanager.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import picto.com.usermanager.domain.user.entity.TitleList;

import java.util.List;

public interface TitleListRepository extends JpaRepository<TitleList, Integer> {
    @Query("select title from TitleList title where title.user.userId = :userId")
    public List<TitleList> findByUserId(@Param("userId") int userId);
}
