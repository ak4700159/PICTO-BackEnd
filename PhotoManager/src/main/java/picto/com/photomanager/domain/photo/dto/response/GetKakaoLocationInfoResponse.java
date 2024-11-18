package picto.com.photomanager.domain.photo.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetKakaoLocationInfoResponse {
    private Meta meta;
    private List<Document> documents;

    @Getter
    @NoArgsConstructor
    public static class Meta {
        private int total_count;
    }

    @Getter
    @NoArgsConstructor
    public static class Document {
        private RoadAddress road_address;
        private Address address;
    }

    @Getter
    @NoArgsConstructor
    public static class RoadAddress {
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

    @Getter
    @NoArgsConstructor
    public static class Address {
        private String address_name;
        private String region_1depth_name;
        private String region_2depth_name;
        private String region_3depth_name;
        private String mountain_yn;
        private String main_address_no;
        private String sub_address_no;
    }
}
