package picto.com.photomanager.domain.photo.application;


import com.amazonaws.services.kms.model.NotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import picto.com.photomanager.domain.photo.dto.response.GetKakaoLocationInfoResponse;


public class LocationService {
    static public GetKakaoLocationInfoResponse searchLocation(double lng, double lat) {
        // 카카오 api로 직접 요청 처리
        // 헤더설정
        final RestTemplate restTemplate = new RestTemplate();
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x=" + lng + "&y=" + lat;
        String accessKey = "88ec86565e1e0ba7d7cf88440d7621e6";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + accessKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 카카오 api 호출 좌표 -> 주소
        HttpEntity<String> entity = new HttpEntity<>(headers);
        GetKakaoLocationInfoResponse info;
        try {
            info = restTemplate
                    .exchange(url, HttpMethod.GET, entity, GetKakaoLocationInfoResponse.class)
                    .getBody();
        }catch (HttpClientErrorException e){
            throw new NotFoundException(e.getMessage());
        }
        // 호출 완료

        // LocationInfo 만들어서 전달
        return info;
    }
}
