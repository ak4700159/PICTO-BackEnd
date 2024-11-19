package picto.com.usermanager.domain.user.dto.response.get.userInfo;

import lombok.Getter;
import picto.com.usermanager.domain.user.entity.UserSetting;

@Getter
public class GetSetting {
    boolean lightMode;
    boolean autoRotation;
    boolean aroundAlert;
    boolean popularAlert;

    public GetSetting(UserSetting userSetting) {
        this.lightMode = userSetting.isLightMode();
        this.autoRotation = userSetting.isAutoRotation();
        this.aroundAlert = userSetting.isAroundAlert();
        this.popularAlert = userSetting.isPopularAlert();
    }
}
