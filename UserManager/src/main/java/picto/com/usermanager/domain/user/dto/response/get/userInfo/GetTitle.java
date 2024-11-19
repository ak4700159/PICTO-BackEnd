package picto.com.usermanager.domain.user.dto.response.get.userInfo;

import lombok.Getter;
import picto.com.usermanager.domain.user.entity.Title;

@Getter
public class GetTitle {
    String titleName;
    String titleContent;

    public GetTitle(Title title) {
        this.titleName = title.getName();
        this.titleContent = title.getContent();
    }
}
