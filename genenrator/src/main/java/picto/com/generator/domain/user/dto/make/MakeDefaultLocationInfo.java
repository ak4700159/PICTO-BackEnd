package picto.com.generator.domain.user.dto.make;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.generator.domain.user.dto.response.GetKakaoLocationInfoResponse;
import picto.com.generator.domain.user.entity.LocationInfo;
import picto.com.generator.domain.user.entity.Photo;

import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MakeDefaultLocationInfo {
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
