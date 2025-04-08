package picto.com.foldermanager.domain.photo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
@Builder
public class PhotoResponse {
    private Long photoId;
    private Long userId;
    private String photoPath;
    private double lat;
    private double lng;
    private String location;
    private String tag;
    private int likes;
    private int views;
    private Long uploadTime;

    public static picto.com.foldermanager.domain.photo.PhotoResponse from(Photo photo) {
        String fileName = photo.getPhotoPath();

        return PhotoResponse.builder()
                .photoId(photo.getPhotoId())
                .userId(photo.getUser().getId())
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