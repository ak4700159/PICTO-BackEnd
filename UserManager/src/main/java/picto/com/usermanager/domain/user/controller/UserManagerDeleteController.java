package picto.com.usermanager.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import picto.com.usermanager.domain.user.application.UserManagerDeleteService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserManagerDeleteController {
    private final UserManagerDeleteService userManagerDeleteService;

    // DELETE METHOD
    // 회원 탈퇴
    @DeleteMapping("/user-manager")
    public ResponseEntity<Map<String, Object>> deleteUser() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/user-manager/mark")
    public ResponseEntity<Map<String, Object>> deleteMark() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }

    // 차단목록 삭제
    @DeleteMapping("/user-manager/block")
    public ResponseEntity<Map<String, Object>> deleteBlock() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }
}
