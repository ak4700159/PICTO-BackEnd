package picto.com.usermanager.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import picto.com.usermanager.domain.user.application.UserManagerLoginService;
import picto.com.usermanager.domain.user.dto.request.SignInRequest;
import picto.com.usermanager.domain.user.dto.request.SignUpRequest;
import picto.com.usermanager.domain.user.dto.response.SignInResponse;
import picto.com.usermanager.domain.user.entity.User;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import java.util.HashMap;
import java.util.Map;

// 로그인 관련 모든 요청 처리

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserManagerLoginController {
    private final UserManagerLoginService userManagerLoginService;

    // 회원가입
    @PostMapping("/user-manager/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        User newUser;
        try {
            log.info("SignUp attempt for user: {} (Name: {})", signUpRequest.getAccountName(),
                    signUpRequest.getName());
            newUser = userManagerLoginService.signUp(signUpRequest);
            userManagerLoginService.addDefaultUser(newUser, signUpRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "회원가입이 완료되었습니다.");
            response.put("user", newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("SignUp failed: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(error);
        }
    }

    // 로그인
    // refresh, access token 발생
    @PostMapping("/user-manager/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        SignInResponse response;
        try {
            log.info("SignIn attempt for user: {}", signInRequest.getUsername());
            response = userManagerLoginService.signIn(signInRequest);
            log.info("SignIn successful for user: {}", signInRequest.getUsername());

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "로그인에 성공했습니다.");
            successResponse.put("data", response);
            return ResponseEntity.ok().body(successResponse);

        } catch (Exception e) {
            log.error("SignIn failed for user: {} - Error: {}", signInRequest.getUsername(), e.getMessage(), e);
            Map<String, String> error = new HashMap<>();

            // 일반적인 인증 오류 메시지 처리
            if (e.getMessage().contains("401") || e.getMessage().contains("Unauthorized")) {
                error.put("error", "비밀번호가 일치하지 않습니다.");
            } else {
                error.put("error", e.getMessage());
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    // 이메일 중복 여부
    @GetMapping("/user-manager/email/{email}")
    public ResponseEntity<String> duplicatedEmail(@PathVariable String email) {
        try {
            userManagerLoginService.verifyDuplicatedUser(email);
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

    // 토큰 인증 api
    @GetMapping("/user-manager/token")
    public String validateToken() {
        return "";
    }

    @PostMapping("/user-manager/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        try {
            String refreshToken = request.get("refreshToken");
            if (refreshToken == null || refreshToken.isEmpty()) {
                throw new IllegalArgumentException("Refresh token is required");
            }

            log.info("Token refresh attempt");
            AccessTokenResponse tokenResponse = userManagerLoginService.refreshToken(refreshToken);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "토큰이 성공적으로 갱신되었습니다.");
            response.put("accessToken", tokenResponse.getToken());

            log.info("Token refresh successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/test")
    public String test() {
        return "test success";
    }
}
