package picto.com.sessionscheduler.domain.session.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.sessionscheduler.domain.session.entity.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {
}
