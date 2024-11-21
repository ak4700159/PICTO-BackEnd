package picto.com.photomanager.global.postDomain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import picto.com.photomanager.global.postDomain.entity.Folder;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    //List<Folder> findByParentId(Long parentId);

    @Query("select f from Folder f where f.user.userId = :userId")
    List<Folder> findByUserId(Long userId);
}
