package picto.com.foldermanager.domain.notice;

import lombok.Getter;

@Getter
public class PushNotificationRequest {
    private String token;
    private String title;
    private String body;
}
