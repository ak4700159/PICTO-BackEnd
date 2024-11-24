package picto.com.usermanager.domain.user.application;


import com.amazonaws.services.kms.model.NotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import picto.com.usermanager.domain.user.dto.response.GetKakaoLocationInfoResponse;

import java.util.Objects;


public class LocationService {
    static public String searchLocation(double lat, double lng) {
        // 카카오 api로 직접 요청 처리
        // 헤더설정
        System.out.println("kakao api called");
        final RestTemplate restTemplate = new RestTemplate();
        String location;
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x=" + lng + "&y=" + lat;
        String accessKey = "88ec86565e1e0ba7d7cf88440d7621e6";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + accessKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 카카오 api 호출 좌표 -> 주소
        HttpEntity<String> entity = new HttpEntity<>(headers);
        GetKakaoLocationInfoResponse info ;
        try {
            info = restTemplate
                    .exchange(url, HttpMethod.GET, entity, GetKakaoLocationInfoResponse.class)
                    .getBody();
            if(Objects.requireNonNull(info).getDocuments().isEmpty()) {
                location = "좌표 식별 불가";
            } else{
                location = info.getDocuments().get(0).getAddress().getAddress_name();
            }
        }catch (HttpClientErrorException e){
            throw new NotFoundException(e.getMessage());
        }
        // 호출 완료

        // LocationInfo 만들어서 전달
        return location;
    }
}
