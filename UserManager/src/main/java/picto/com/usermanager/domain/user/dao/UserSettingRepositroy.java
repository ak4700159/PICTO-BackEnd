package picto.com.usermanager.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.usermanager.domain.user.entity.UserSetting;

public interface UserSettingRepositroy extends JpaRepository<UserSetting, Long> {
}
