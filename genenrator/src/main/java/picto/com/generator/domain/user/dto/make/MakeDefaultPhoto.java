package picto.com.generator.domain.user.dto.make;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.generator.domain.user.dto.response.GetKakaoLocationInfoResponse;
import picto.com.generator.domain.user.entity.Photo;
import picto.com.generator.domain.user.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MakeDefaultPhoto {
    private Long photoId;
    private String photoPath;
    private String s3FileName;
    private double lat;
    private double lng;
    private String location;
    private Long registerDatetime;
    private Long updateDatetime;
    private boolean frame_active;
    private boolean shared_active;
    private int likes;
    private int views;
    private String title;
    private String tag;

    public Map<String, Object> toRandomPhoto(Long userIdNum, Long photoIdNum, User user, GetKakaoLocationInfoResponse kakaoResponse){
        Random random = new Random();
        Map<String, Object> result = new HashMap<>();

        photoPath = "테스트";

        likes = random.nextInt(5000);
        if(likes > 0){
            views = random.nextInt(likes * 2) + likes;
        }else{
            views = random.nextInt(12000);
        }

        // 대구에서 임의의 위도 경도 값(좌표) 추출 후 kakao api를 통해 지역명을 가지고 온다.
        lat = random.nextDouble(35.88682728 - 35.77475029) + 35.77475029;
        lng = random.nextDouble(128.6355584 - 128.4313995) +  128.4313995;
        // 밑에 문장은 비용이 많이 들 것이다... --> static function 으로 변환
        if(Objects.requireNonNull(kakaoResponse).getDocuments().isEmpty()) {
            location = "좌표 식별 불가";
        } else{
            location = kakaoResponse.getDocuments().get(0).getAddress().getAddress_name();
        }
        result.put("kakaoResponse", kakaoResponse);

        // 프레임 여부는 비활성화
        // 공유 여부는 photoId가 3의 배수일때 true
        frame_active = false;
        shared_active = (new Random().nextInt() % 2 == 0);

        title = userIdNum + "'s photo title";
        updateDatetime = System.currentTimeMillis();
        registerDatetime = System.currentTimeMillis();

        // 카테고리 3분할
        if(new Random().nextInt(10)  % 3 == 0)
            tag = "돼지";
        else if(new Random().nextInt(10)  % 3 == 1)
            tag = "강아지";
        else{
            tag = "고양이";
        }

        Photo newPhoto = Photo
                .builder()
                .tag(tag)
                .user(user)
                .lat(lat)
                .lng(lng)
                .registerDatetime(registerDatetime)
                .uploadDatetime(updateDatetime)
                .likes(likes)
                .views(views)
                .location(location)
                .photoPath(photoPath)
                .frameActive(frame_active)
                .sharedActive(shared_active)
                .build();
        result.put("photo", newPhoto);
        return result;
    }
}
