package picto.com.foldermanager.domain.photo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
@Builder
public class PhotoResponse {
    private Long photoId;
    private String photoPath;
    private String contentType;
    private double lat;
    private double lng;
    private String location;
    private String tag;
    private int likes;
    private int views;
    private Long uploadTime;

    public static picto.com.foldermanager.domain.photo.PhotoResponse from(Photo photo) {
        String fileName = photo.getS3FileName();
        String contentType = determineContentType(fileName);

        return picto.com.foldermanager.domain.photo.PhotoResponse.builder()
                .photoId(photo.getPhotoId())
                .photoPath(photo.getPhotoPath())
                .contentType(contentType)
                .lat(photo.getLat())
                .lng(photo.getLng())
                .location(photo.getLocation())
                .tag(photo.getTag())
                .likes(photo.getLikes())
                .views(photo.getViews())
                .uploadTime(photo.getUploadDatetime())
                .build();
    }

    private static String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "png" -> MediaType.IMAGE_PNG_VALUE;
            case "gif" -> MediaType.IMAGE_GIF_VALUE;
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG_VALUE;
            default -> "application/octet-stream";
        };
    }
}