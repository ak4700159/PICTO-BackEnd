package picto.com.generator.global.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.global.entity.UserSetting;

public interface UserSettingRepositroy extends JpaRepository<UserSetting, Integer> {
}
