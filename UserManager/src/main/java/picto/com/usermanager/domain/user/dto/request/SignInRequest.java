package picto.com.usermanager.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 로그인 요청
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInRequest {
    private String username;
    private String email;
    private String password;

    @Builder
    public SignInRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
