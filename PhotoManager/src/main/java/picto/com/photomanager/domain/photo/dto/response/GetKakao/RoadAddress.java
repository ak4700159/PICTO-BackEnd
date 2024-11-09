package picto.com.photomanager.domain.photo.dto.response.GetKakao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoadAddress {
    private String address_name;
    private String region_1depth_name;
    private String region_2depth_name;
    private String region_3depth_name;
    private String road_name;
    private String underground_yn;
    private String main_building_no;
    private String sub_building_no;
    private String building_name;
    private String zone_no;
}