package picto.com.photomanager.global.postDomain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.photomanager.global.postDomain.entity.PhotoRecord;
import picto.com.photomanager.global.postDomain.entity.PhotoRecordId;

public interface PhotoRecordRepository extends JpaRepository<PhotoRecord, PhotoRecordId> {
}
