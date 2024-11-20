package picto.com.photomanager.global.postDomain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.photomanager.global.postDomain.entity.Folder;
import picto.com.photomanager.global.postDomain.entity.FolderId;

public interface FolderRepository extends JpaRepository<Folder, FolderId> {
}
