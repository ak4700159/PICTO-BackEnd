package picto.com.photostore.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
@Builder
public class PhotoResponse {
    private Long photoId;
    private String photoPath;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String contentType;
    private double lat;
    private double lng;
    private String tag;
    private int likes;
    private int views;
    private Long uploadTime;

    public static PhotoResponse from(Photo photo) {
        return PhotoResponse.builder()
                .photoId(photo.getPhotoId())
                .photoPath(photo.getPhotoPath())
                .contentType(determineContentType(photo.getS3FileName()))
                .lat(photo.getLat())
                .lng(photo.getLng())
                .tag(photo.getTag())
                .likes(photo.getLikes())
                .views(photo.getViews())
                .uploadTime(photo.getUploadDatetime())
                .build();
    }

    public static PhotoResponse fromWithContentType(Photo photo, String contentType) {
        return PhotoResponse.builder()
                .photoId(photo.getPhotoId())
                .photoPath(photo.getPhotoPath())
                .contentType(contentType)
                .lat(photo.getLat())
                .lng(photo.getLng())
                .tag(photo.getTag())
                .likes(photo.getLikes())
                .views(photo.getViews())
                .uploadTime(photo.getUploadDatetime())
                .build();
    }

    private static String determineContentType(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return null;
        }
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "png" -> MediaType.IMAGE_PNG_VALUE;
            case "gif" -> MediaType.IMAGE_GIF_VALUE;
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG_VALUE;
            default -> null;
        };
    }
}