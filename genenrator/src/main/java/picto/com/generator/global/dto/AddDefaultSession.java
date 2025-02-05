package picto.com.generator.global.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.generator.domain.user.entity.User;
import picto.com.generator.global.entity.Session;

import java.util.Random;

// 대구광역시 위도(latitude) 경도(longitude) 로 기본 설정
// 위도 : 35.77475029 ~ 35.88682728 , 경도 : 128.4313995 ~ 128.6355584

@Getter
@NoArgsConstructor
public class AddDefaultSession {
    public Session toEntity(User newUser, double lat, double lng, String location) {
        return Session.builder().
                user(newUser).
                currentLat(lat).
                currentLng(lng).
                location(location).
                active(false).
                build();
    }
}
