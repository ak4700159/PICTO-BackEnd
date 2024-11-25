package picto.com.usermanager.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import picto.com.usermanager.domain.user.application.UserManagerDeleteService;
import picto.com.usermanager.domain.user.dto.request.EventRequest;
import picto.com.usermanager.domain.user.dto.request.USerRequest;

@RestController
@RequiredArgsConstructor
public class UserManagerDeleteController {
    private final UserManagerDeleteService userManagerDeleteService;

    // DELETE METHOD
    // 회원 탈퇴
    @DeleteMapping("/user-manager/user")
    public ResponseEntity<?> deleteUser(@RequestBody USerRequest request) {
        try {
            userManagerDeleteService.deleteUSer(request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("success");
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/user-manager/mark")
    public ResponseEntity<?> deleteMark(@RequestBody EventRequest request) {
        try {
            userManagerDeleteService.deleteMark(request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("success");
    }

    // 차단목록 삭제
    @DeleteMapping("/user-manager/block")
    public ResponseEntity<?> deleteBlock(@RequestBody EventRequest request) {
        try {
            userManagerDeleteService.deleteBlock(request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("success");
    }
}
