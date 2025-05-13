package picto.com.generator.domain.user.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picto.com.generator.domain.user.dao.*;
import picto.com.generator.domain.user.dto.make.*;
import picto.com.generator.domain.user.dto.response.GetKakaoLocationInfoResponse;
import picto.com.generator.domain.user.entity.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GeneratorUserService {
    // 무조건 final 로 설정해야 롬북이 작동함.
    private final UserRepository userRepository;
    private final TagSelectRepositroy tagSelectRepositroy;
    private final FilterRepository filterRepository;
    private final SessionRepository sessionRepository;
    private final UserSettingRepositroy userSettingRepositroy;
    private final LocationService locationService;

    @Transactional
    public User makeUser() {
        User newUser = new MakeDefaultUser().toRandomEntity(0L);
        userRepository.save(newUser);
        System.out.println("User created");
        return newUser;
    }

    @Transactional
    public void makeFilter(User user){
        Filter filter = new MakeDefaultFilter().toEntity(user);
        filterRepository.save(filter);
    }

    @Transactional
    public void makeUserSetting(User user) {
        UserSetting userSetting = new MakeDefaultUserSetting().toEntity(user);
        userSettingRepositroy.save(userSetting);
    }

    @Transactional
    public void makeSession(User user) {
        Random rand = new Random();

        double lat, lng;
        if (rand.nextInt(20) == 0) {
            // 제주도
            lat = rand.nextDouble(33.55321855 - 33.11130278) + 33.11130278;
            lng = rand.nextDouble(126.95101599 - 126.14484406) + 126.14484406;
        } else {
            // 제주도 제외
            lat = rand.nextDouble(38.61244627 - 34.22222699) + 34.22222699;
            lng = rand.nextDouble(129.58312748 - 126.10356267) + 126.10356267;
        }

        GetKakaoLocationInfoResponse kakaoResponse = locationService.searchLocation(lng, lat);

        String location;
        if(Objects.requireNonNull(kakaoResponse).getDocuments().isEmpty()) {
            location = "좌표 식별 불가";
        } else{
            location = kakaoResponse.getDocuments().get(0).getAddress().getAddress_name();
        }

        Session session = new MakeDefaultSession().toEntity(user, lat, lng, location);
        sessionRepository.save(session);
    }

    @Transactional
    public void makeTagSelect(User user){
        // 초기 사용자 선택된 태그는 [개 고양이 새]
        TagSelect tagSelect = new MakeDefaultTagSelect().toEntity(user, "개");
        tagSelectRepositroy.save(tagSelect);
        TagSelect tagSelect2 = new MakeDefaultTagSelect().toEntity(user, "고양이");
        tagSelectRepositroy.save(tagSelect2);
        TagSelect tagSelect3 = new MakeDefaultTagSelect().toEntity(user, "새");
        tagSelectRepositroy.save(tagSelect3);
    }
}