package picto.com.photomanager.domain.photo.dto.response.GetKakao;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Address {
    private String address_name;
    private String region_1depth_name;
    private String region_2depth_name;
    private String region_3depth_name;
    private String mountain_yn;
    private String main_address_no;
    private String sub_address_no;
}
