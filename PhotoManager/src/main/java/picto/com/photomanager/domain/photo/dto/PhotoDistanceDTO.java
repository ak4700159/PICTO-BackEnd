package picto.com.photomanager.domain.photo.dto;

import lombok.Getter;
import picto.com.photomanager.domain.photo.entity.Photo;

@Getter
public class PhotoDistanceDTO extends Photo {
    private int distance;

    public PhotoDistanceDTO(Long photoId, String photoPath, double lat, double lng,
                            String location, Long registerDatetime, Long uploadDatetime, String tag,
                            boolean frameActive, boolean sharedActive, int likes, int views, int distance) {
        // 작성 ~
        builder().
                photoId(photoId).
                photoPath(photoPath).
                lat(lat).
                lng(lng).
                location(location).
                registerDatetime(registerDatetime).
                uploadDatetime(uploadDatetime).
                tag(tag).
                frameActive(frameActive).
                sharedActive(sharedActive).
                likes(likes).
                views(views).
                build();
        this.distance = distance;
    }

}
