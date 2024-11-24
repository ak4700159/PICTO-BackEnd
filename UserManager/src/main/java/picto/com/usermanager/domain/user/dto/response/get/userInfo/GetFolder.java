package picto.com.usermanager.domain.user.dto.response.get.userInfo;

import lombok.Getter;
import picto.com.usermanager.domain.user.entity.Folder;
import picto.com.usermanager.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetFolder {
    Long folderId;
    Long createdDatetime;
    String folderName;
    String folderContent;
    List<Long> members;

    public GetFolder(Folder folder, List<Long> members) {
        this.folderId = folder.getFolderId();
        this.createdDatetime = folder.getCreatedDatetime();
        this.folderName = folder.getName();
        this.folderContent = folder.getContent();
        this.members = members;
    }
}
