package picto.com.foldermanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.foldermanager.domain.notice.Notice;
import picto.com.foldermanager.domain.user.User;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByReceiverOrderByCreatedDatetimeAsc(User receiver);
}