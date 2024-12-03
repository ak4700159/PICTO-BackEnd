package picto.com.foldermanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import picto.com.foldermanager.domain.photo.Photo;
import picto.com.foldermanager.domain.user.User;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByUser(User user);
}