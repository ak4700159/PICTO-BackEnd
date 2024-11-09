package picto.com.photomanager.domain.photo.dto.request;


import com.amazonaws.services.kms.model.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import picto.com.photomanager.domain.photo.dto.response.GetKakao.GetKakaoLocationInfoResponse;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.domain.photo.entity.PhotoId;
import picto.com.photomanager.global.user.entity.User;

import java.util.Random;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddTestPhotoRequest {
    final private RestTemplate restTemplate = new RestTemplate();
    private final String accessKey = "88ec86565e1e0ba7d7cf88440d7621e6";

    private PhotoId photoId;
    private String photoPath;
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

    public Photo toRandomPhoto(int userIdNum, int photoIdNum, User user){
        Random random = new Random();
        photoPath = "s3://picto-test-bucket/picto-photos/20210115_104549.jpg";

        likes = random.nextInt(10000);
        views = random.nextInt(10000);

        lat = random.nextDouble(35.88682728 - 35.77475029) + 35.77475029;
        lng = random.nextDouble(128.6355584 - 128.4313995) +  128.4313995;

        // 카카오 api로 직접 요청 처리
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x=" + lng + "&y=" + lat;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + accessKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        GetKakaoLocationInfoResponse info;
        try {
            info = restTemplate
                    .exchange(url, HttpMethod.GET , entity, GetKakaoLocationInfoResponse.class)
                    .getBody();
        }catch (HttpClientErrorException e){
            throw new NotFoundException(e.getMessage());
        }
        System.out.println(info);
        if(info == null) throw new NullPointerException();
        location = info.getDocuments().get(0).getAddress().getAddress_name();
        System.out.println("location: " + location);

        frame_active = false;
        shared_active = userIdNum % 3 == 0;

        title = userIdNum + "'s photo title";
        updateDatetime = System.currentTimeMillis();
        registerDatetime = System.currentTimeMillis();

        photoId = new PhotoId(photoIdNum, userIdNum);
        if(photoIdNum % 3 == 0)
            tag = "돼지";
        else if(photoIdNum % 3 == 1)
            tag = "개";
        else{
            tag = "고양이";
        }

        return Photo
                .builder()
                .tag(tag)
                .user(user)
                .photoId(photoId)
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
    }
}
