package picto.com.chattingscheduler.domain.session.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import picto.com.chattingscheduler.domain.session.entity.Share;
import picto.com.chattingscheduler.domain.session.entity.ShareId;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, ShareId> {
    @Query("select share from Share share where share.id.folderId = :folderId and share.id.userId = :userId")
    List<Share> findByFolderId(Long folderId, Long userId);
}
