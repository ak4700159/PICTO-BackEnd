package picto.com.chattingscheduler.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.chattingscheduler.entity.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {
}
