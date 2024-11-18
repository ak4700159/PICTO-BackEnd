package picto.com.usermanager.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import picto.com.usermanager.domain.user.application.UserService;
import picto.com.usermanager.domain.user.dto.request.SignInRequest;
import picto.com.usermanager.domain.user.dto.request.SignUpRequest;
import picto.com.usermanager.domain.user.dto.response.SignInResponse;
import picto.com.usermanager.domain.user.entity.User;

// 로그인 관련 모든 요청 처리

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<User> signUp(SignUpRequest signUpRequest) {
        User newUser;
        try {
            newUser = userService.signUp(signUpRequest);
            userService.addDefault(newUser, signUpRequest);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<SignInResponse> signIn(SignInRequest signInRequest) {
        SignInResponse response;
        try {
            response = userService.signIn(signInRequest);
        }catch (IllegalAccessException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(response);
    }

    // 비밀번호 찾기
    @GetMapping("/passwd")
    public String restorePasswd() {
        return "";
    }


    // 이메일 인증
    @GetMapping("/email")
    public String restoreEmail() {
        return "";
    }
}
