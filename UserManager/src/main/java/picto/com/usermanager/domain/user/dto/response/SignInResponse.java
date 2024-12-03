package picto.com.usermanager.domain.user.dto.response;

import lombok.Getter;

// 로그인 응답
@Getter
public class SignInResponse {
    String accessToken;
    Long userId;

    public SignInResponse(String accessToken, Long userId) {
        this.userId = userId;
        this.accessToken = accessToken;
    }
}
