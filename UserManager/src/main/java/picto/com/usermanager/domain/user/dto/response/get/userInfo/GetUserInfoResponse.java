package picto.com.usermanager.domain.user.dto.response.get.userInfo;

import lombok.Builder;
import lombok.Getter;
import picto.com.usermanager.domain.user.dao.ShareRepository;
import picto.com.usermanager.domain.user.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
public class GetUserInfoResponse {
    private final GetUser user;
    private final GetFilter filter;
    private final GetSetting userSetting;
    private final List<String> tags;
    private final List<GetFolderPhoto> folderPhotos;

    @Builder
    public GetUserInfoResponse(User user, UserSetting setting, Filter filter,
                               List<TagSelect> tags, Map<Folder, List<Photo>> folderPhoto, List<Photo> myPhotos) {
        this.user = new GetUser(user);
        this.filter = new GetFilter(filter);
        this.userSetting = new GetSetting(setting);

        this.tags = new ArrayList<String>();
        for (TagSelect tag : tags) {
            this.tags.add(tag.getTagSelectedId().getTag());
        }

        this.folderPhotos = new ArrayList<GetFolderPhoto>();
        for (Folder folder : folderPhoto.keySet()) {
            for (Photo photo : folderPhoto.get(folder)) {
                folderPhotos.add(new GetFolderPhoto(folder, photo));
            }
        }

        for(Photo photo : myPhotos) {
            folderPhotos.add(new GetFolderPhoto(null, photo));
        }
    }
}
