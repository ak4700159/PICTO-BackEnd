package picto.com.usermanager.domain.user.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import picto.com.usermanager.domain.user.dao.*;
import picto.com.usermanager.domain.user.dto.request.SignInRequest;
import picto.com.usermanager.domain.user.dto.request.SignUpRequest;
import picto.com.usermanager.domain.user.dto.response.SignInResponse;
import picto.com.usermanager.domain.user.entity.*;
import picto.com.usermanager.global.utils.JwtUtilImpl;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtUtilImpl jwtUtil;

    // 기본 세팅을 위한 레파지토리
    private final FilterRepository filterRepository;
    private final UserSettingRepositroy userSettingRepository;
    private final TagSelectRepositroy tagSelectRepositroy;
    private final SessionRepository sessionRepository;

    // 사용자 디폴트값 설정
    @Transactional
    public void addDefault(User newUser, SignUpRequest signUpRequest) throws IllegalAccessException {
        assert newUser.getId() != null;
        User referUser = userRepository.getReferenceById(newUser.getId());
        Filter defualFilter = Filter.toEntity(referUser);
        UserSetting defaultSetting = UserSetting.toEntity(referUser);
        TagSelect defaultTag = TagSelect.toEntity(referUser, "돼지");
        Session defaultSession = Session.toEntity(referUser, signUpRequest.getLat(), signUpRequest.getLng());

        String accessToken = jwtUtil.createToken();
        System.out.println(accessToken.length());
        try{
            filterRepository.save(defualFilter);
            userSettingRepository.save(defaultSetting);
            tagSelectRepositroy.save(defaultTag);
            sessionRepository.save(defaultSession);
            tokenRepository.save(Token
                    .builder()
                    .accessToken(accessToken)
                    .refreshToken("")
                    .user(referUser)
                    .build());
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
            System.out.println("newUserId : " + newUser.getEmail());
            userRepository.save(newUser);
        }
        catch (Exception e){
            throw new Exception("DuplicatedId");
        }

        // Controller에서 나머지 사용자 디폴트값 설정
        return newUser;
    }


    @Transactional
    public SignInResponse signIn(SignInRequest signInRequest) throws IllegalAccessException {
        User findUser = userRepository.findByEmail(signInRequest.getEmail());
        Token userToken;
        if (findUser == null) {
            throw new IllegalAccessException("NotFoundUser");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(signInRequest.getPassword(), findUser.getPassword())) {
            throw new IllegalAccessException("NotMatchingPassword");
        }
        userToken = tokenRepository.getReferenceById(Objects.requireNonNull(findUser.getId()));

        // 토큰 발행
        return new SignInResponse(userToken.getAccessToken());
    }

    @Transactional
    public boolean verifyDuplicatedUser(String userEmail) throws IllegalAccessException {
        if(userRepository.findByEmail(userEmail) != null) {
            System.out.println("중복된 유저");
            throw new IllegalAccessException("Duplicated");
        }
        return true;
    }
}
