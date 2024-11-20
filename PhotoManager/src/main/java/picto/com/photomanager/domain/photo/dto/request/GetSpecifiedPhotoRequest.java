package picto.com.photomanager.domain.photo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetSpecifiedPhotoRequest {
    // 전송하는 사용자 아이이
    Long senderId;

    // owner : user : photo
    String eventType;

    // 해당 타입의 식별자
    Long eventTypeId;
}
