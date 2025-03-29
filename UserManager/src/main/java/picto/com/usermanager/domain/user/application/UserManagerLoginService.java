package picto.com.usermanager.domain.user.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import picto.com.usermanager.domain.user.dao.*;
import picto.com.usermanager.domain.user.dto.request.SignInRequest;
import picto.com.usermanager.domain.user.dto.request.SignUpRequest;
import picto.com.usermanager.domain.user.dto.response.GetKakaoLocationInfoResponse;
import picto.com.usermanager.domain.user.dto.response.SignInResponse;
import picto.com.usermanager.domain.user.entity.*;
import picto.com.usermanager.global.utils.JwtUtilImpl;
import picto.com.usermanager.global.utils.KakaoUtil;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserManagerLoginService {
    private final KakaoUtil kakaoUtil;
    private final JwtUtilImpl jwtUtil;
    // 기본 세팅을 위한 레파지토리
    private final UserRepository userRepository;
    private final FilterRepository filterRepository;
    private final UserSettingRepositroy userSettingRepository;
    private final TagSelectRepositroy tagSelectRepositroy;
    private final SessionRepository sessionRepository;

    // 사용자 디폴트값 설정
    @Transactional
    public void addDefaultUser(User newUser, SignUpRequest signUpRequest) throws IllegalAccessException {
        User referUser = userRepository.getUserByEmail(newUser.getEmail());
        Filter defualFilter = Filter.toEntity(referUser);
        UserSetting defaultSetting = UserSetting.toEntity(referUser);
        TagSelect defaultTag = TagSelect.toEntity(referUser, "돼지");

        double lat = signUpRequest.getLat();
        double lng = signUpRequest.getLng();
        String location;
        GetKakaoLocationInfoResponse response = kakaoUtil.convertLocationFromPos(lat, lng);
        if(Objects.requireNonNull(response).getDocuments().isEmpty()) {
            location = "좌표 식별 불가";
        } else{
            location = response.getDocuments().get(0).getAddress().getAddress_name();
        }
        Session defaultSession = Session.toEntity(referUser, lat, lng, location);

        try{
            filterRepository.save(defualFilter);
            userSettingRepository.save(defaultSetting);
            tagSelectRepositroy.save(defaultTag);
            sessionRepository.save(defaultSession);
        }catch (IllegalArgumentException e){
            System.out.println("Default Setting Error");
            throw new IllegalAccessException();
        }
    }

    @Transactional
    public User signUp(SignUpRequest signUpRequest) throws Exception {
        try{
            verifyDuplicatedUser(signUpRequest.getEmail());
        }
        catch (Exception e){
            throw new Exception("DuplicatedEmail");
        }

        // DB 전체 수정 필요 -> UUID로 설정해야함
        //String uuid = UUID.randomUUID().toString();

        // 단방향 암호화
        // 법적으로 DB에 비밀번호를 저장할 때 무조건 암호화 후 저장되어야 한다.
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPwd = passwordEncoder.encode(signUpRequest.getPassword());

        // user 생성 후 저장
        User newUser = User.toMakeEntity(signUpRequest.getName(), signUpRequest.getEmail(), hashedPwd);
        System.out.println(newUser);
        try{
            newUser = userRepository.save(newUser);
        }
        catch (Exception e){
            throw new Exception("DuplicatedId");
        }

        // Controller에서 나머지 사용자 디폴트값 설정
        return newUser;
    }

    @Transactional
    public SignInResponse signIn(SignInRequest signInRequest) throws IllegalAccessException {
        // 존재하는 사용자인지 검증
        User findUser = userRepository.getUserByEmail(signInRequest.getEmail());
        if (findUser == null) {
            throw new IllegalAccessException("NotFoundUser");
        }

        // 비밀번호 매치
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(signInRequest.getPassword(), findUser.getPassword())) {
            throw new IllegalAccessException("NotMatchingPassword");
        }

        // 토큰 생성 로직
        jwtUtil.setUserId(findUser.getId());
        jwtUtil.setAccess(true);
        String accessToken = jwtUtil.createToken();

        jwtUtil.setAccess(false);
        String refreshToken = jwtUtil.createToken();
        return new SignInResponse(accessToken, refreshToken, findUser.getUserId());
    }

    @Transactional
    public void verifyDuplicatedUser(String userEmail) throws IllegalAccessException {
        if(userRepository.getUserByEmail(userEmail) != null) {
            System.out.println("중복된 유저");
            throw new IllegalAccessException("Duplicated");
        }
    }
}
