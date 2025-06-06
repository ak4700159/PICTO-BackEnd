package picto.com.usermanager.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordPatchRequest {
    private String email;
    private String password;
    private String newPassword;
}
