package picto.com.usermanager.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @PostMapping("/user-manager/signup")
    public ResponseEntity<User> signUp(@RequestBody SignUpRequest signUpRequest) {
        User newUser;
        try {
            newUser = userService.signUp(signUpRequest);
            userService.addDefault(newUser, signUpRequest);
        }catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).header("message", e.getMessage()).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // 로그인
    @PostMapping("/user-manager/signin")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signInRequest) {
        SignInResponse response;

        try {
            response = userService.signIn(signInRequest);
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.notFound().header("message", e.getMessage()).build();
        }
        return ResponseEntity.ok().body(response);
    }

    // 이메일 중복 여부
    @GetMapping("/user-manager/email/{email}")
    public ResponseEntity<String> duplicatedEmail(@PathVariable String email) {
        try {
            userService.verifyDuplicatedUser(email);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).header("message", e.getMessage()).build();
        }
        String result = "true";
        return ResponseEntity.ok(result);
    }

    // 비밀번호 찾기
    @GetMapping("/user-manager/passwd")
    public String restorePasswd() {
        return "";
    }


    // 이메일 인증
    @GetMapping("/user-manager/email")
    public String restoreEmail() {
        return "";
    }
}
