package picto.com.generator.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.domain.user.entity.Share;
import picto.com.generator.domain.user.entity.ShareId;


public interface ShareRepository extends JpaRepository<Share, ShareId> {
}
