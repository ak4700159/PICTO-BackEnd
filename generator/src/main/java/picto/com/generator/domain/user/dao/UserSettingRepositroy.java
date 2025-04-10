package picto.com.generator.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.domain.user.entity.UserSetting;

public interface UserSettingRepositroy extends JpaRepository<UserSetting, Long> {
}
