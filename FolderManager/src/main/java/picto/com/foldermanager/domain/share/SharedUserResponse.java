package picto.com.foldermanager.domain.share;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SharedUserResponse {
    private Long userId;
    private Long folderId;
    private Long sharedDatetime;

    public static SharedUserResponse from(Share share) {
        return SharedUserResponse.builder()
                .userId(share.getUser().getId())
                .folderId(share.getFolder().getId())
                .sharedDatetime(share.getSharedDatetime())
                .build();
    }
}