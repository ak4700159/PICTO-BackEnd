package picto.com.usermanager.domain.user.dto.response;

import lombok.Getter;

// 로그인 응답
@Getter
public class SignInResponse {
    String accessToken;
    String refreshToken;
    Long userId;

    public SignInResponse(String accessToken, String refreshToken, Long userId) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.userId = userId;
    }
}
