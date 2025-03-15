package picto.com.usermanager.domain.user.dto.response.get.userInfo;

import lombok.Getter;
import picto.com.usermanager.domain.user.entity.Folder;
import picto.com.usermanager.domain.user.entity.Photo;

@Getter
public class GetFolderPhoto extends GetPhoto{
    private final Long folderId;
    public GetFolderPhoto(Folder folder, Photo photo) {
        super(photo);
        this.folderId = folder.getFolderId();
    }
}
