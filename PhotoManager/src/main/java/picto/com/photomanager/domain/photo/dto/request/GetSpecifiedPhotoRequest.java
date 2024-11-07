package picto.com.photomanager.domain.photo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetSpecifiedPhotoRequest {
    // 전송하는 사용자
    String senderId;
    // 유저인지 사진인지
    String type;
    // 해당 타입의 식별자
    int typeId;
}
