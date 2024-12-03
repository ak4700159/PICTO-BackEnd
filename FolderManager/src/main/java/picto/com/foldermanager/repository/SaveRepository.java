package picto.com.foldermanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import picto.com.foldermanager.domain.folder.Folder;
import picto.com.foldermanager.domain.photo.Photo;
import picto.com.foldermanager.domain.save.Save;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaveRepository extends JpaRepository<Save, Long> {
    List<Save> findAllByFolder(Folder folder);
    Optional<Save> findByFolderAndPhoto_PhotoId(Folder folder, Long photoId);
    void deleteAllByFolder(Folder folder);
    boolean existsByFolderAndPhoto(Folder folder, Photo photo);
}