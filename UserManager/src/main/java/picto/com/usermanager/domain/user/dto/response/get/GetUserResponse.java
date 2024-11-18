package picto.com.usermanager.domain.user.dto.response.get;

import lombok.Getter;
import picto.com.usermanager.domain.user.entity.*;

import java.util.List;

@Getter
public class GetUserResponse {
    private User user;
    private Filter filter;
    private UserSetting userSetting;
    private List<TagSelect> tags;
    private List<TitleList> titles;
    private List<Photo> photos;

}
