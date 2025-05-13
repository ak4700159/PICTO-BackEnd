package picto.com.generator.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.domain.user.entity.PhotoRecord;
import picto.com.generator.domain.user.entity.PhotoRecordId;

public interface PhotoRecordRepository extends JpaRepository<PhotoRecord, PhotoRecordId> {
}
