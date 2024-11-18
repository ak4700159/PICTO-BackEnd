package picto.com.usermanager.domain.user.dto.response;

import lombok.Getter;

// 로그인 응답
@Getter
public class SignInResponse {
    String accessToken;

    public SignInResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
