package picto.com.usermanager.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 사용자 정보 수정 및 삭제
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPatchRequest {
    private String name;
    private String accountName;
    private String email;
    private String password;
    private Boolean profileActive;
    private String profilePhotoPath;
    private String intro;
}
