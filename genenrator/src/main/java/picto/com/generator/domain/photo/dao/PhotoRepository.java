package picto.com.generator.domain.photo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import picto.com.generator.domain.photo.domain.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, String> {
}
