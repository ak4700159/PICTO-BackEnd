package picto.com.photostore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.photostore.domain.folder.Folder;
import picto.com.photostore.domain.photo.Photo;
import picto.com.photostore.domain.user.User;
import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByUser(User user);
    List<Photo> findByUserAndFrameActiveTrue(User user);
    long countByUserAndFrameActiveTrue(User user);
    long countByUserAndFrameActiveFalse(User user);
}