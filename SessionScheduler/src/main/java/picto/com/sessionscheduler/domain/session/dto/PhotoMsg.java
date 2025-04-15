package picto.com.sessionscheduler.domain.session.dto;


import lombok.Builder;
import lombok.Data;
import picto.com.sessionscheduler.domain.session.entity.Photo;

@Data
@Builder
public class PhotoMsg {
    private Long photoId;
    private String tag;
    private String location;
    private String photoPath;
    private double lat;
    private double lng;
    private long uploadTime;
    private int likes;
    private int views;

    public static PhotoMsg fromEntity(Photo photo) {
        return PhotoMsg.builder()
                .photoId(photo.getPhotoId())
                .tag(photo.getTag())
                .location(photo.getLocation())
                .photoPath(photo.getPhotoPath())
                .lat(photo.getLat())
                .lng(photo.getLng())
                .uploadTime(photo.getUploadDatetime())
                .likes(photo.getLikes())
                .views(photo.getViews())
                .build();
    }
}
