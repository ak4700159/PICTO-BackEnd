package picto.com.photomanager.global.postDomain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.photomanager.global.postDomain.entity.Share;
import picto.com.photomanager.global.postDomain.entity.ShareId;

public interface ShareRepository extends JpaRepository<Share, ShareId> {
}
