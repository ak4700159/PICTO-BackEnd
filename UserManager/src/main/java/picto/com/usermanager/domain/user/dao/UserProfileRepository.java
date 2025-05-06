package picto.com.usermanager.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import picto.com.usermanager.domain.user.entity.UserProfile;
import picto.com.usermanager.domain.user.entity.UserProfileId;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UserProfileId> {
    Long getUserProfileByUserId(Long userId);
}
