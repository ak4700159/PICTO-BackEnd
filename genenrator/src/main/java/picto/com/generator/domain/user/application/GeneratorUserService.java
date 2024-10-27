package picto.com.generator.domain.user.application;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import picto.com.generator.domain.user.dao.UserRepository;
import picto.com.generator.domain.user.domain.User;
import picto.com.generator.domain.user.dto.MakeUserRequest;
import picto.com.generator.global.dto.*;
import picto.com.generator.global.models.*;
import picto.com.generator.global.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeneratorUserService {
    // 무조건 final 로 설정해야 롬북이 작동함.
    private final UserRepository userRepository;
    private final TagSelectRepositroy tagSelectRepositroy;
    private final FilterRepositroy filterRepositroy;
    private final SessionRepository sessionRepository;
    private final TokenRepository tokenRepository;
    private final UserSettingRepositroy userSettingRepositroy;

    //private final EntityManager em;
    @Transactional
    public User addUser(int user_id){
        User newUser = new MakeUserRequest().toRandomEntity(user_id);
        userRepository.save(newUser);
        //em.persist(newUser);
        // 기본 필터 저장 [정렬 : 좋아요순] / [기간 : 한 달] / [start_date : 생성기준 UTC]
        Filter filter = new AddDefaultFilter().toEntity(newUser);
        filterRepositroy.save(filter);

        // 초기 사용자 선택된 태그는 [돼지 고양이 강아지]
        TagSelect tagSelect = new AddDefaultTagSelect().toEntity(newUser, "돼지");
        tagSelectRepositroy.save(tagSelect);
        TagSelect tagSelect2 = new AddDefaultTagSelect().toEntity(newUser, "강아지");
        tagSelectRepositroy.save(tagSelect2);
        TagSelect tagSelect3 = new AddDefaultTagSelect().toEntity(newUser, "고양이");
        tagSelectRepositroy.save(tagSelect3);

        // 대구광역시 위도(latitude) 경도(longitude) 로 기본 설정
        // 위도 : 35.77475029 ~ 35.88682728 , 경도 : 128.4313995 ~ 128.6355584
        Session session = new AddDefaultSession().toEntity(newUser);
        sessionRepository.save(session);
        //
        Token toke = new AddDefaultToken().toEntity(newUser);
        tokenRepository.save(toke);

        UserSetting userSetting = new AddDefaultUserSetting().toEntity(newUser);
        userSettingRepositroy.save(userSetting);
        return newUser;
    }

    public List<User> makeUser100(){
        List<User> users = new ArrayList<User>();
        for(int i = 1; i <= 100; i++){
            User newUser = addUser(i);
            System.out.println("생성 : " + i);
            users.add(newUser);

//            setBasicUser(i);
//            System.out.println(i + " 설정 완료");
        }
        return users;
    }

    public void setBasicUser(int user_id){
        User newUser = userRepository.getReferenceById(user_id);
        // 기본 필터 저장 [정렬 : 좋아요순] / [기간 : 한 달] / [start_date : 생성기준 UTC]
        Filter filter = new AddDefaultFilter().toEntity(newUser);
        filterRepositroy.save(filter);

        // 초기 사용자 선택된 태그는 [돼지 고양이 강아지]
        TagSelect tagSelect = new AddDefaultTagSelect().toEntity(newUser, "돼지");
        tagSelectRepositroy.save(tagSelect);
        TagSelect tagSelect2 = new AddDefaultTagSelect().toEntity(newUser, "강아지");
        tagSelectRepositroy.save(tagSelect2);
        TagSelect tagSelect3 = new AddDefaultTagSelect().toEntity(newUser, "고양이");
        tagSelectRepositroy.save(tagSelect3);

        // 대구광역시 위도(latitude) 경도(longitude) 로 기본 설정
        // 위도 : 35.77475029 ~ 35.88682728 , 경도 : 128.4313995 ~ 128.6355584
        Session session = new AddDefaultSession().toEntity(newUser);
        sessionRepository.save(session);
        //
        Token toke = new AddDefaultToken().toEntity(newUser);
        tokenRepository.save(toke);

        UserSetting userSetting = new AddDefaultUserSetting().toEntity(newUser);
        userSettingRepositroy.save(userSetting);
    }

    public List<User> findEmailUsers(String email){
        return userRepository.findEmailName(email);
    }

    public List<User> findAllUser(){
        return userRepository.findAll();
    }
}
