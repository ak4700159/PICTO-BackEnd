package picto.com.photomanager.global.postDomain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.global.postDomain.entity.PhotoRecord;
import picto.com.photomanager.global.postDomain.entity.PhotoRecordId;

import java.util.List;

public interface PhotoRecordRepository extends JpaRepository<PhotoRecord, PhotoRecordId> {
    @Query("select p from PhotoRecord p where p.id.agentId = :userId")
    List<PhotoRecord> findByUserId(@Param("userId") Long userId);
}
