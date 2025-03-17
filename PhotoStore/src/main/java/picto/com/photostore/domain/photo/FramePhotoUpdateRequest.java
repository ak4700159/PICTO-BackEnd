package picto.com.photostore.domain.photo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FramePhotoUpdateRequest {
    private String tag;
    private Long registerTime;
    private boolean frameActive;
    private boolean sharedActive;
}