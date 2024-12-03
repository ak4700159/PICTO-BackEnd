package picto.com.foldermanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import picto.com.foldermanager.domain.folder.Folder;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Optional<Folder> findByLink(String link);
    List<Folder> findAllByOrderByIdAsc();
}