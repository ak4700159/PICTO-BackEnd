package picto.com.foldermanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import picto.com.foldermanager.domain.folder.Folder;
import picto.com.foldermanager.domain.share.Share;
import picto.com.foldermanager.domain.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {
    List<Share> findAllByFolder(Folder folder);
    void deleteAllByFolder(Folder folder);
    boolean existsByUserAndFolder(User user, Folder folder);
    Optional<Share> findByUserAndFolder(User user, Folder folder);
}