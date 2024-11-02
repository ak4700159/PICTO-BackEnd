package picto.com.photomanager.domain.photo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAroundPhotoRequest {
    String type;
    String typeId;
    // 수 많은 이미지 스트림을 어떻게 처리할 것인지 나중에 고려
    //int count;
}
