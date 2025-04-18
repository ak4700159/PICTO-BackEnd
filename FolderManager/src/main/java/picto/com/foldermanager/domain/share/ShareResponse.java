package picto.com.foldermanager.domain.share;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShareResponse {
    private Long userId;
    private Long folderId;
    private String folderName;
    private Long sharedDatetime;
    private String content;

    public static picto.com.foldermanager.domain.share.ShareResponse from(Share share) {
        return ShareResponse.builder()
                .userId(share.getUser().getId())
                .folderId(share.getFolder().getId())
                .folderName(share.getFolder().getName())
                .sharedDatetime(share.getSharedDatetime())
                .content(share.getFolder().getContent())
                .build();
    }
}