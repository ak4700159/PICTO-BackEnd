package picto.com.generator.domain.user.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import picto.com.generator.domain.user.dto.response.GetKakaoLocationInfoResponse;

@Service
@Slf4j
public class LocationService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    public GetKakaoLocationInfoResponse searchLocation(double lng, double lat) {
        final RestTemplate restTemplate = new RestTemplate();

        String url = String.format(
                "https://dapi.kakao.com/v2/local/geo/coord2address.json?x=%f&y=%f&input_coord=WGS84",
                lng, lat
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            log.info("Requesting Kakao API with URL: {}", url);

            return restTemplate
                    .exchange(url, HttpMethod.GET, entity, GetKakaoLocationInfoResponse.class)
                    .getBody();

        } catch (Exception e) {
            log.error("Error calling Kakao API", e);
            throw new RuntimeException("Failed to get location info from Kakao API", e);
        }
    }
}
