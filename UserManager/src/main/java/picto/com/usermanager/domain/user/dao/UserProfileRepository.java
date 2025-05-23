package picto.com.usermanager.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import picto.com.usermanager.domain.user.entity.UserProfile;
import picto.com.usermanager.domain.user.entity.UserProfileId;

import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UserProfileId> {
    List<UserProfile> getUserProfileByUserId(Long userId);
}
