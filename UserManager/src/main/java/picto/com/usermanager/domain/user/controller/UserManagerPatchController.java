package picto.com.usermanager.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import picto.com.usermanager.domain.user.application.UserManagerPatchService;
import picto.com.usermanager.domain.user.dto.request.USerRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserManagerPatchController {
    final UserManagerPatchService userManagerPatchService;
    // PUT METHOD
    @PatchMapping("/user-manager/user")
    public ResponseEntity<?> modifyUser(@RequestBody USerRequest request) {
        try{
            userManagerPatchService.fetchUser(request);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("success");
    }

    @PatchMapping("/user-manager/setting")
    public ResponseEntity<?> modifySetting() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }

    @PatchMapping("/user-manager/title-list")
    public ResponseEntity<Map<String, Object>> modifyTitleList() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }

    @PatchMapping("/user-manager/filter")
    public ResponseEntity<Map<String, Object>> modifyFilter() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }

    // 즐겨찾기 추가
    @PatchMapping("/user-manager/mark")
    public ResponseEntity<Map<String, Object>> modifyMark() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }

    // 차단목록 추가
    @PatchMapping("/user-manager/block")
    public ResponseEntity<Map<String, Object>> modifyBlock() {
        return ResponseEntity.ok(new HashMap<String, Object>() {});
    }
}
