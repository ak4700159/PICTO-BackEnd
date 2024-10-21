package picto.com.genenrator.domain.photo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.genenrator.domain.photo.domain.Photo;
import picto.com.genenrator.domain.photo.domain.PhotoId;

public interface PhotoRepository extends JpaRepository<Photo, PhotoId> {
}
