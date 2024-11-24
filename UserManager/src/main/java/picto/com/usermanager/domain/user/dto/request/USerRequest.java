package picto.com.usermanager.domain.user.dto.request;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 사용자 정보 수정 및 삭제
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class USerRequest {
    private Long userId;
    private String name;
    private String accountName;
    private String email;
    private String password;
    private Boolean profileActive;
    private String profilePhotoPath;
    private String intro;

    // 요청의 종류 :
    // ,password
    // ,info[name, accountName, email, profileActive, intro, profilePath, profilePhotoPath
    private String type;
}
