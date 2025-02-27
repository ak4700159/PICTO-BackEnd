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
    private final int MAX_USERS = 500;
    private final LocationService locationService;

    @Transactional
    public ArrayList<User> makeUserN(){
        ArrayList<User> users = new ArrayList<User>();
        for(long i = 0; i < MAX_USERS; i++){
            User newUser = new MakeUserDefault().toRandomEntity(i);
            userRepository.save(newUser);
            users.add(newUser);
        }
        System.out.println("User created");
        return users;
    }

    @Transactional
    public void makeFilterN(ArrayList<User> users){
        System.out.println(users.get(0).getName());
        for(int i = 0; i < users.toArray().length; i++){
            User user = userRepository.getReferenceById(users.get(i).getUserId());
            // 기본 필터 저장 [정렬 : 좋아요순] / [기간 : 한달] / [start_datetime : 생성기준 UTC]
            Filter filter = new MakeDefaultFilter().toEntity(user);
            filterRepository.save(filter);
        }
    }

    @Transactional
    public void makeTagSelectN(ArrayList<User> users){
        for(int i = 0; i < users.size(); i++){
            User user = userRepository.getReferenceById(users.get(i).getUserId());
            // 초기 사용자 선택된 태그는 [돼지 고양이 강아지]
            TagSelect tagSelect = new MakeDefaultTagSelect().toEntity(user, "돼지");
            tagSelectRepositroy.save(tagSelect);
            TagSelect tagSelect2 = new MakeDefaultTagSelect().toEntity(user, "강아지");
            tagSelectRepositroy.save(tagSelect2);
            TagSelect tagSelect3 = new MakeDefaultTagSelect().toEntity(user, "고양이");
            tagSelectRepositroy.save(tagSelect3);
        }
    }

    @Transactional
    public void makeSessionN(ArrayList<User> users){
        Random rand = new Random();
        for(int i = 0; i < users.size(); i++){
            User user = userRepository.getReferenceById(users.get(i).getUserId());
            double lat = rand.nextDouble(35.88682728 - 35.77475029) + 35.77475029;
            double lng = rand.nextDouble(128.6355584 - 128.4313995) +  128.4313995;
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
    }

    @Transactional
    public void makeUserSettingN(ArrayList<User> users){
        for(int i = 0; i < users.size(); i++){
            User user = userRepository.getReferenceById(users.get(i).getUserId());
            UserSetting userSetting = new MakeDefaultUserSetting().toEntity(user);
            userSettingRepositroy.save(userSetting);
        }
    }
}
