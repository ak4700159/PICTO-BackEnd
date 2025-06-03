package picto.com.usermanager.domain.user.dto.response;

import lombok.Getter;

// 로그인 응답
@Getter
public class EmailVerificationResponse {
    String message;
    boolean isSuccess;

    public EmailVerificationResponse(String message, boolean isSuccess) {
        this.message = message;
        this.isSuccess = isSuccess;
    }
}
