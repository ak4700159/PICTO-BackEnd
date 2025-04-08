package picto.com.foldermanager.domain.folder;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FolderResponse {
    private Long folderId;
    private Long userId;
    private String folderName;
    private String content;
    private Long sharedDatetime;

    public static picto.com.foldermanager.domain.folder.FolderResponse from(Folder folder) {
        return FolderResponse.builder()
                .folderId(folder.getId())
                .userId(folder.getGenerator().getId())
                .folderName(folder.getName())
                .content(folder.getContent())
                .sharedDatetime(folder.getCreatedDatetime())
                .build();
    }
}