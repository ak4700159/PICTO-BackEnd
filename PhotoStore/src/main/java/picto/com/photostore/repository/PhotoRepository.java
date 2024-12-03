package picto.com.photostore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.photostore.domain.Photo;
import picto.com.photostore.domain.User;
import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByUser(User user);
    List<Photo> findByUserAndFrameActiveTrue(User user);
    long countByUserAndFrameActiveTrue(User user);
}