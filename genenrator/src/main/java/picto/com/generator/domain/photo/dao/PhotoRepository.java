package picto.com.generator.domain.photo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.domain.photo.domain.Photo;

public interface PhotoRepository extends JpaRepository<Photo, String> {
}
