package picto.com.photomanager.domain.photo.dto.response;


import lombok.Getter;
import picto.com.photomanager.domain.photo.dto.PhotoDistanceDTO;
import picto.com.photomanager.domain.photo.dto.PhotoLikeRankingDTO;
import picto.com.photomanager.domain.photo.entity.Photo;

@Getter
public class GetPhotoResponse {
    private final Long photoId;
    private final Long userId;
    private final String photoPath;
    private final double lat;
    private final double lng;
    private final String location;
    private final Long registerDatetime;
    private final Long updateDatetime;
    private final boolean frame_active;
    private final int likes;
    private final int views;
    private final String tag;

    // 공유중인 사진에 대해서만 반환한다.
    //private boolean shared_active;

    public GetPhotoResponse(Photo photo) {
        this.photoId = photo.getPhotoId();
        this.userId = photo.getUserId();
        this.photoPath = photo.getPhotoPath();
        this.lat = photo.getLat();
        this.lng = photo.getLng();
        this.location = photo.getLocation();
        this.registerDatetime = photo.getRegisterDatetime();
        this.updateDatetime = photo.getUploadDatetime();
        this.frame_active = photo.isFrameActive();
        this.likes = photo.getLikes();
        this.views = photo.getViews();
        this.tag = photo.getTag();
    }

    static public GetPhotoResponse fromRankingDTO(PhotoLikeRankingDTO photo) {
        return new GetPhotoResponse((Photo)photo);
    }

    static public GetPhotoResponse fromDistanceDTO(PhotoDistanceDTO photo) {
        return new GetPhotoResponse((Photo)photo);
    }
}
