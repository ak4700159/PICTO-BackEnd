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

    public Map<String, Object> toRandomPhoto(Long userIdNum, Long photoIdNum, User user,
                                             GetKakaoLocationInfoResponse kakaoResponse,
                                             double lat, double lng, String tag){
        Random random = new Random();
        Map<String, Object> result = new HashMap<>();

        photoPath = "테스트";

        likes = random.nextInt(5000);
        if(likes > 0){
            views = random.nextInt(likes * 2) + likes;
        }else{
            views = random.nextInt(12000);
        }

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

//        // 카테고리 3분할
//        if(new Random().nextInt(10)  % 3 == 0)
//            tag = "개";
//        else if(new Random().nextInt(10)  % 3 == 1)
//            tag = "고양이";
//        else{
//            tag = "새";
//        }
        this.tag=tag;

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
