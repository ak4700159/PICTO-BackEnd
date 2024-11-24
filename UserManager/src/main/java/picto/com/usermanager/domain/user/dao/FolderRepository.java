package picto.com.usermanager.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import picto.com.usermanager.domain.user.entity.Folder;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    @Query("select f from Folder f where f.user.userId = :userId")
    List<Folder> findByUserId(Long userId);
}
