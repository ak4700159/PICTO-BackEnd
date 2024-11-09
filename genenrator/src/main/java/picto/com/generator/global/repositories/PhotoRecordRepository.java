package picto.com.generator.global.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.global.entity.PhotoRecord;
import picto.com.generator.global.entity.PhotoRecordId;

public interface PhotoRecordRepository extends JpaRepository<PhotoRecord, PhotoRecordId> {
}
