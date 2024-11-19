package picto.com.usermanager.domain.user.dto.response.get.userInfo;

import lombok.Getter;
import picto.com.usermanager.domain.user.entity.User;

@Getter
public class GetUser {
    private Integer usreId;
    // 실명
    private String name;
    // 계명 별치
    private String accountName;
    private String email;
    private boolean profileActive;
    private String intro;
    //private String password;


    public GetUser(User user) {
        this.usreId = user.getId();
        this.name = user.getName();
        this.accountName = user.getAccountName();
        this.email = user.getEmail();
        this.profileActive = user.isProfileActive();
        this.intro = user.getIntro();
    }

}
