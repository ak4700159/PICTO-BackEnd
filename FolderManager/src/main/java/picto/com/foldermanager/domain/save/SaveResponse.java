package picto.com.foldermanager.domain.save;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaveResponse {
    private Long photoId;
    private Long userId;
    private Long folderId;
    private Long savedDatetime;

    public static picto.com.foldermanager.domain.save.SaveResponse from(Save save) {
        return picto.com.foldermanager.domain.save.SaveResponse.builder()
                .photoId(save.getPhoto().getPhotoId())
                .userId(save.getPhoto().getUser().getId())
                .folderId(save.getFolder().getId())
                .savedDatetime(save.getSavedDatetime())
                .build();
    }
}