package picto.com.photomanager.domain.photo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetRepresentativePhotoRequest {
    String type;
    String target;
    int count;
}
