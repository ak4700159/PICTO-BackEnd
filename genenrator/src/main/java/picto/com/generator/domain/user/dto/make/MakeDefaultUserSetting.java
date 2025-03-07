package picto.com.generator.domain.user.dto.make;

import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.generator.domain.user.entity.User;
import picto.com.generator.domain.user.entity.UserSetting;

@Getter
@NoArgsConstructor
public class MakeDefaultUserSetting {
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
