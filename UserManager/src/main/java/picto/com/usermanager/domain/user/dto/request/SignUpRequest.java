package picto.com.usermanager.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 회원가입시
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {
    private String email;
    private String password;
    private String name;

    private double lat;
    private double lng;


    @Builder
    public SignUpRequest(String email, String password, String name, double lat, double lng) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }
}
