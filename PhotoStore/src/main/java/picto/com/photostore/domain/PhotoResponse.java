package picto.com.photostore.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PhotoResponse {
    private Long photoId;
    private String photoPath;
    private double lat;
    private double lng;
    private String location;
    private String tag;
    private int likes;
    private int views;
    private Long uploadTime;

    public static PhotoResponse from(Photo photo) {
        return PhotoResponse.builder()
                .photoId(photo.getPhotoId())
                .photoPath(photo.getPhotoPath())
                .lat(photo.getLat())
                .lng(photo.getLng())
                .location(photo.getLocation())
                .tag(photo.getTag())
                .likes(photo.getLikes())
                .views(photo.getViews())
                .uploadTime(photo.getUploadDatetime())
                .build();
    }
}
