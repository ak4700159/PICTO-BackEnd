package picto.com.photomanager.domain.photo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAroundPhotoRequest {
    // 요청 사용자 아이디
    int sendId;
    // photo or user
    String type;
    // 해당 타입의 아이디
    int typeId;
    // 수 많은 이미지 스트림을 어떻게 처리할 것인지 나중에 고려
    //int count;
}
