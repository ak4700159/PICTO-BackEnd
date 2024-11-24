package picto.com.usermanager.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import picto.com.usermanager.domain.user.entity.Share;
import picto.com.usermanager.domain.user.entity.ShareId;

import java.util.List;


public interface ShareRepository extends JpaRepository<Share, ShareId> {
    @Query("select  share from Share share where share.id.userId = :userId ")
    List<Share> findByUserId(Long userId);
}
