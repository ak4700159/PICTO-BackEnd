package picto.com.foldermanager.domain.share;

import lombok.Builder;
import lombok.Getter;
import picto.com.foldermanager.domain.folder.Folder;

@Getter
@Builder
public class ShareResponse {
    private Long generatorId;
    private Long folderId;
    private String folderName;
    private Long sharedDatetime;
    private String content;

    public static picto.com.foldermanager.domain.share.ShareResponse from(Share share) {
        return ShareResponse.builder()
                .generatorId(share.getFolder().getGenerator().getId())
                .folderId(share.getFolder().getId())
                .folderName(share.getFolder().getName())
                .sharedDatetime(share.getSharedDatetime())
                .content(share.getFolder().getContent())
                .build();
    }

    public static picto.com.foldermanager.domain.share.ShareResponse from(Folder folder) {
        return ShareResponse.builder()
                .generatorId(folder.getGenerator().getId())
                .folderId(folder.getId())
                .folderName(folder.getName())
                .sharedDatetime(folder.getCreatedDatetime())
                .content(folder.getContent())
                .build();
    }
}
