package picto.com.usermanager.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import picto.com.usermanager.domain.user.application.UserManagerGetService;
import picto.com.usermanager.domain.user.dto.response.get.userInfo.GetUser;
import picto.com.usermanager.domain.user.dto.response.get.userInfo.GetUserInfoResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserManagerGetController {
    private final UserManagerGetService userManagerGetService;

    // GET METHOD
    @GetMapping("/user-manager/user-all/{userId}")
    public ResponseEntity<GetUserInfoResponse> getUser(@PathVariable("userId") Long userId) {
        GetUserInfoResponse response;
        try{
            response = userManagerGetService.getUser(userId);
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.notFound().header("message", e.getMessage()).build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-manager/users/{userId}")
    public ResponseEntity<GetUser> getOtherUser(@PathVariable("userId") Long userId){
        GetUser user;
        try{
            user = userManagerGetService.GetOtherUserById(userId);
            if(!user.isProfileActive()){
                return ResponseEntity.notFound().header("message", "isProfiledFalse").build();
            }
        } catch (Exception e){
            return ResponseEntity.badRequest().header("message", e.getMessage()).build();
        }
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/user-manager/users")
    public ResponseEntity<GetUser> getOtherUser(@RequestBody Map<String, Object> result){
        GetUser user;
        try{
            user = userManagerGetService.GetOtherUserByEmail(result.get("email").toString());
//            if(!user.isProfileActive()){
//                return ResponseEntity.notFound().header("message", "isProfiledFalse").build();
//            }
        } catch (Exception e){
            return ResponseEntity.badRequest().header("message", e.getMessage()).build();
        }
        return ResponseEntity.ok().body(user);
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
