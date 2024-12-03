package picto.com.foldermanager.domain.share;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShareResponse {
    private Long userId;
    private Long folderId;
    private Long sharedDatetime;

    public static picto.com.foldermanager.domain.share.ShareResponse from(Share share) {
        return picto.com.foldermanager.domain.share.ShareResponse.builder()
                .userId(share.getUser().getId())
                .folderId(share.getFolder().getId())
                .sharedDatetime(share.getSharedDatetime())
                .build();
    }
}