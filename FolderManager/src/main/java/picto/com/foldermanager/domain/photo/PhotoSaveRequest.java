package picto.com.foldermanager.domain.photo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PhotoSaveRequest {
    private Long photoId;
    private Long userId;
    private Long folderId;
}