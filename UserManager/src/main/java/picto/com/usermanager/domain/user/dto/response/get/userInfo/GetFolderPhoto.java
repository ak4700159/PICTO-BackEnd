package picto.com.usermanager.domain.user.dto.response.get.userInfo;

import lombok.Getter;
import picto.com.usermanager.domain.user.entity.Folder;
import picto.com.usermanager.domain.user.entity.Photo;

@Getter
public class GetFolderPhoto extends GetPhoto{
    private Long folderId;
    public GetFolderPhoto(Folder folder, Photo photo) {
        super(photo);
        if(folder != null) {
            this.folderId = folder.getFolderId();
        }
    }
}
