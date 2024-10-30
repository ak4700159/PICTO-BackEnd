package picto.com.generator.global.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.generator.domain.user.domain.User;
import picto.com.generator.global.models.UserSetting;

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
