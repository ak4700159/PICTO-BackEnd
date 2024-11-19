package picto.com.usermanager.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import picto.com.usermanager.domain.user.application.UserManagerGetService;
import picto.com.usermanager.domain.user.dto.response.get.userInfo.GetUserInfoResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserManagerGetController {
    private final UserManagerGetService userManagerGetService;

    // GET METHOD
    @GetMapping("/user-manager/users/{userId}")
    public ResponseEntity<GetUserInfoResponse> getUser(@PathVariable("userId") Integer userId) {
        GetUserInfoResponse response = userManagerGetService.getUser(userId);
        return ResponseEntity.ok(response);
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
