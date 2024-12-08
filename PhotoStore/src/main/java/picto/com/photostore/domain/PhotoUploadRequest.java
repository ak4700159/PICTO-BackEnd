package picto.com.photostore.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoUploadRequest {
    private Long userId;
    private double lat;
    private double lng;
    private String tag;
    private Long registerTime;
    private boolean frameActive;
    private boolean sharedActive;
}
