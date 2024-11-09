package picto.com.generator.domain.user.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picto.com.generator.domain.user.dao.UserRepository;
import picto.com.generator.domain.user.entity.User;
import picto.com.generator.domain.user.dto.MakeUserRequest;
import picto.com.generator.global.dto.*;
import picto.com.generator.global.entity.*;
import picto.com.generator.global.repositories.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneratorUserService {
    // 무조건 final 로 설정해야 롬북이 작동함.
    private final UserRepository userRepository;
    private final TagSelectRepositroy tagSelectRepositroy;
    private final FilterRepository filterRepository;
    private final SessionRepository sessionRepository;
    private final TokenRepository tokenRepository;
    private final UserSettingRepositroy userSettingRepositroy;
    private final int MAX_USERS = 500;

    @Transactional
    public List<User> makeUserN(){
        List<User> users = new ArrayList<User>();
        for(int i = 1; i <= MAX_USERS; i++){
            User newUser = new MakeUserRequest().toRandomEntity(i);
            userRepository.save(newUser);
        }
        System.out.println("User created");
        return users;
    }

    @Transactional
    public void makeFilterN(){
        for(int i = 1; i <= MAX_USERS; i++){
            User user = userRepository.getReferenceById(i);
            // 기본 필터 저장 [정렬 : 좋아요순] / [기간 : 한 달] / [start_date : 생성기준 UTC]
            Filter filter = new AddDefaultFilter().toEntity(user);
            filterRepository.save(filter);
        }
    }

    @Transactional
    public void makeTagSelectN(){
        for(int i = 1; i < MAX_USERS; i++){
            User user = userRepository.getReferenceById(i);
            // 초기 사용자 선택된 태그는 [돼지 고양이 강아지]
            TagSelect tagSelect = new AddDefaultTagSelect().toEntity(user, "돼지");
            tagSelectRepositroy.save(tagSelect);
            TagSelect tagSelect2 = new AddDefaultTagSelect().toEntity(user, "강아지");
            tagSelectRepositroy.save(tagSelect2);
            TagSelect tagSelect3 = new AddDefaultTagSelect().toEntity(user, "길고양이");
            tagSelectRepositroy.save(tagSelect3);
        }
    }

    @Transactional
    public void makeSessionN(){
        for(int i = 1; i <= MAX_USERS; i++){
            User user = userRepository.getReferenceById(i);
            // 대구광역시 위도(latitude) 경도(longitude) 로 기본 설정
            // 위도 : 35.77475029 ~ 35.88682728 , 경도 : 128.4313995 ~ 128.6355584
            Session session = new AddDefaultSession().toEntity(user);
            sessionRepository.save(session);
        }
    }

    @Transactional
    public void makeTokenN(){
        for(int i = 1; i <= MAX_USERS; i++){
            User user = userRepository.getReferenceById(i);
            Token token = new AddDefaultToken().toEntity(user);
            tokenRepository.save(token);
        }
    }

    @Transactional
    public void makeUserSettingN(){
        for (int i = 1; i < MAX_USERS; i++) {
            User user = userRepository.getReferenceById(i);
            UserSetting userSetting = new AddDefaultUserSetting().toEntity(user);
            userSettingRepositroy.save(userSetting);
        }
    }

    public List<User> findEmailUsers(String email){
        return userRepository.findEmailName(email);
    }

    public List<User> findAllUser(){
        return userRepository.findAll();
    }
}
