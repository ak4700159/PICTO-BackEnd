package picto.com.generator.domain.photo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.domain.photo.domain.Photo;
import picto.com.generator.domain.photo.domain.PhotoId;

public interface PhotoRepository extends JpaRepository<Photo, PhotoId> {
}
