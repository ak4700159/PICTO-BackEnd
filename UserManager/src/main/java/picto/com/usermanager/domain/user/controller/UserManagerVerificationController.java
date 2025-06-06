package picto.com.usermanager.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import picto.com.usermanager.domain.user.application.UserManagerVerificationService;
import picto.com.usermanager.domain.user.dto.response.EmailVerificationResponse;

import lombok.extern.slf4j.Slf4j;

// 이메일 인증 관련 모든 요청 처리

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserManagerVerificationController {
    private final UserManagerVerificationService userManagerVerificationService;

    // 이메일 중복 여부
    @GetMapping("/user-manager/email/{email}")
    public ResponseEntity<String> duplicatedEmail(@PathVariable String email) {
        try {
            userManagerVerificationService.verifyDuplicatedUser(email);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).header("message", e.getMessage()).build();
        }
        String result = "true";
        return ResponseEntity.ok(result);
    }

    @PostMapping("/user-manager/send-verify-email/{email}")
    public ResponseEntity<String> sendVerifyEmail(@PathVariable("email") String email) {
        userManagerVerificationService.sendVerifyEmail(email);
        return ResponseEntity.ok("이메일 인증 메일이 발송되었습니다.");
    }

    @PostMapping("/user-manager/verify-email")
    public ResponseEntity<EmailVerificationResponse> verifyEmail(@RequestParam String email,
            @RequestParam String code) {
        EmailVerificationResponse isSuccess = userManagerVerificationService.verifyEmail(email, code);
        return ResponseEntity.ok(isSuccess);
    }

    @GetMapping("/user-manager/is-verified-email/{email}")
    public ResponseEntity<EmailVerificationResponse> isVerifiedEmail(@PathVariable String email) {
        EmailVerificationResponse isVerified = userManagerVerificationService.isVerifiedEmail(email);
        return ResponseEntity.ok(isVerified);
    }

    @PostMapping("/user-manager/send-temporary-password/{email}")
    public ResponseEntity<String> sendTemporaryPassword(@PathVariable("email") String email) {
        userManagerVerificationService.sendTemporaryPassword(email);
        return ResponseEntity.ok("임시 비밀번호 발송 완료");
    }
}
