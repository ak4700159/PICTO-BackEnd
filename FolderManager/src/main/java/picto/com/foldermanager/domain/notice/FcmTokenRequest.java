package picto.com.foldermanager.domain.notice;

public class FcmTokenRequest {
    private Long userId;
    private String fcmToken;

    public Long getUserId() {
        return userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }
}
