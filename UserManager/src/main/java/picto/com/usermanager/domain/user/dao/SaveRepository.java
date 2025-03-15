package picto.com.usermanager.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import picto.com.usermanager.domain.user.entity.Save;
import picto.com.usermanager.domain.user.entity.SaveId;

import java.util.List;


public interface SaveRepository extends JpaRepository<Save, SaveId> {
    @Query("select s from Save s where s.id.folderId = :folderId")
    public List<Save> getSaveByFolderId(@Param("folderId") Long folderId);
}
