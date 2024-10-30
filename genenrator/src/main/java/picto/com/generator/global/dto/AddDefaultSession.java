package picto.com.generator.global.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.generator.domain.user.domain.User;
import picto.com.generator.global.models.Session;

import java.util.Random;

// 대구광역시 위도(latitude) 경도(longitude) 로 기본 설정
// 위도 : 35.77475029 ~ 35.88682728 , 경도 : 128.4313995 ~ 128.6355584

@Getter
@NoArgsConstructor
public class AddDefaultSession {
    public Session toEntity(User newUser) {
        Random rand = new Random();
        double lat = rand.nextDouble(35.88682728 - 35.77475029) + 35.77475029;
        double lng = rand.nextDouble(128.6355584 - 128.4313995) +  128.4313995;

        return Session.builder().
                user(newUser).
                currentLat(lat).
                currentLng(lng).
                location("대구광역시 달성군 옥포읍").
                active(false).
                build();
    }
}
