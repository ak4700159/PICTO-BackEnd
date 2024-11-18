package picto.com.usermanager.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserManagerGetController {
    // GET METHOD
    @GetMapping("/user-manager")
    public ResponseEntity<Map<String, Object>> getUser() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }

    @GetMapping("/user-manager/setting")
    public ResponseEntity<Map<String, Object>> getUserSetting() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }

    @GetMapping("/user-manager/title-list")
    public ResponseEntity<Map<String, Object>> getUserTitleList() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }

    @GetMapping("/user-manager/filter")
    public ResponseEntity<Map<String, Object>> getUserFilter() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }
    // 즐겨찾기 조회
    @GetMapping("/user-manager/mark")
    public ResponseEntity<Map<String, Object>> getUserMark() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }

    // 차단목록 조회
    @GetMapping("/user-manager/block")
    public ResponseEntity<Map<String, Object>> getUserBlock() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }
}
