package picto.com.foldermanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import picto.com.foldermanager.domain.folder.Folder;
import picto.com.foldermanager.domain.user.User;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findAllByOrderByIdAsc();
    boolean existsByGeneratorAndName(User generator, String name);
    List<Folder> findAllByGenerator(User generator);
}
