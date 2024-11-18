package picto.com.usermanager.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserManagerPatchController {
    // PUT METHOD
    @PutMapping("/user-manager")
    public ResponseEntity<Map<String, Object>> modifyUser() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }

    @PutMapping("/user-manager/setting")
    public ResponseEntity<Map<String, Object>> modifySetting() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }

    @PutMapping("/user-manager/title-list")
    public ResponseEntity<Map<String, Object>> modifyTitleList() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }

    @PutMapping("/user-manager/filter")
    public ResponseEntity<Map<String, Object>> modifyFilter() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }

    // 즐겨찾기 추가
    @PutMapping("/user-manager/mark")
    public ResponseEntity<Map<String, Object>> modifyMark() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }

    // 차단목록 추가
    @PutMapping("/user-manager/block")
    public ResponseEntity<Map<String, Object>> modifyBlock() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }
}
