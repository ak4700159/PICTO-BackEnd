package picto.com.usermanager.domain.user.dto.response.get.userInfo;

import lombok.Getter;
import picto.com.usermanager.domain.user.entity.TagSelect;

@Getter
public class GetTag {
    String tagName;

    public GetTag(TagSelect tagSelect) {
        this.tagName = getTagName();
    }
}
