package picto.com.photomanager.domain.photo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.photomanager.domain.photo.dto.response.GetKakaoLocationInfoResponse;
import picto.com.photomanager.domain.photo.entity.LocationInfo;
import picto.com.photomanager.domain.photo.entity.Photo;

import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddTestLocationInfoRequest {
    private Long id;
    private String largeName;
    private String middleName;
    private String smallName;

    public LocationInfo toEntity(Long userId, Long photoId, GetKakaoLocationInfoResponse info, Photo photo) {
        if (Objects.requireNonNull(info).getDocuments().isEmpty()) {
            largeName = "카카오 api 오류";
            middleName = "카카오 api 오류";
            smallName = "카카오 api 오류";
        } else {
            largeName = info.getDocuments().get(0).getAddress().getRegion_1depth_name();
            middleName = info.getDocuments().get(0).getAddress().getRegion_2depth_name();
            smallName = info.getDocuments().get(0).getAddress().getRegion_3depth_name().split(" ")[0];
        }
        return LocationInfo.
                builder().
                largeName(largeName).
                middleName(middleName).
                smallName(smallName).
                photo(photo).
                photoId(photoId).
                build();
    }
}
