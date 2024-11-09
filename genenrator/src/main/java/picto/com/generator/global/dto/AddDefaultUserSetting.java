package picto.com.generator.global.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.generator.domain.user.entity.User;
import picto.com.generator.global.entity.UserSetting;

@Getter
@NoArgsConstructor
public class AddDefaultUserSetting {
    public UserSetting toEntity(User newUser){
        return UserSetting.
                builder().
                user(newUser).
                lightMode(false).
                autoRotation(false).
                aroundAlert(true).
                popularAlert(true).
                build();
    }
}
