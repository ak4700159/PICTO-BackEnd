package picto.com.generator.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import picto.com.generator.domain.user.entity.Folder;
import picto.com.generator.domain.user.entity.User;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    //List<Folder> findByParentId(Long parentId);

    List<Folder> findByUser(User user);
}
