package picto.com.photomanager.domain.photo.dto;

import lombok.Getter;

@Getter
public class PhotoEventDTO {
    // type = click , unClick
    String type;
    Long userId;
    Long photoId;
}
