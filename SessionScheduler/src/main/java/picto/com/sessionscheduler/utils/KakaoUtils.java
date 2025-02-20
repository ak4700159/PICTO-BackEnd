package picto.com.sessionscheduler.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import picto.com.sessionscheduler.domain.session.dto.GetKakaoLocationInfoResponse;

@Component
public class KakaoUtils {
    @Value("${kakao.access}")
    private String kakaoAccess;

    public GetKakaoLocationInfoResponse convertLocationFromPos(double lng, double lat) {
        // 카카오 api로 직접 요청 처리
        // 헤더설정
        final RestTemplate restTemplate = new RestTemplate();
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x=" + lng + "&y=" + lat;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoAccess);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 카카오 api 호출 좌표 -> 주소 반환
        HttpEntity<String> entity = new HttpEntity<>(headers);
        GetKakaoLocationInfoResponse info;
        try {
            info = restTemplate
                    .exchange(url, HttpMethod.GET, entity, GetKakaoLocationInfoResponse.class)
                    .getBody();
        }catch (HttpClientErrorException e){
            throw new HttpClientErrorException(e.getStatusCode());
        }
        // 호출 완료

        // LocationInfo 만들어서 전달
        return info;
    }
}
