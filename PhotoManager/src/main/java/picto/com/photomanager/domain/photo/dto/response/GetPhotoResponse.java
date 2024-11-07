package picto.com.photomanager.domain.photo.dto.response;


import lombok.Getter;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.domain.photo.entity.PhotoId;

@Getter
public class GetPhotoResponse {
    private final int photoId;
    private final int userId;
    private final String photoPath;
    private final double lat;
    private final double lng;
    private final String location;
    private final Long registerDatetime;
    private final Long updateDatetime;
    private final boolean frame_active;
    private final int likes;
    private final int views;
    private final String title;
    private final String tag;

    // 공유중인 사진에 대해선 유효하기 때문에 고려 X
    //private boolean shared_active;

    public GetPhotoResponse(Photo photo) {
        this.photoId = photo.getId().getPhotoId();
        this.userId = photo.getId().getPhotoId();
        this.photoPath = photo.getPhotoPath();
        this.lat = photo.getLat();
        this.lng = photo.getLng();
        this.location = photo.getLocation();
        this.registerDatetime = photo.getRegisterDatetime();
        this.updateDatetime = photo.getUploadDatetime();
        this.frame_active = photo.isFrameActive();
        this.likes = photo.getLikes();
        this.views = photo.getViews();
        this.title = photo.getTitle();
        this.tag = photo.getTag();
    }
}
