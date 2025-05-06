package picto.com.usermanager.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import picto.com.usermanager.domain.user.application.UserProfileService;

@RestController
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @PutMapping("/user-manager/profile/photo")
    public ResponseEntity<String> updateUserProfile(@RequestParam("userId") Long userId, @RequestParam("photoId") Long photoId) {
        try {
            userProfileService.updateProfilePhoto(userId, photoId);
            return ResponseEntity.ok("Profile photo updated successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(404)
                    .body("프로필 사진 업데이트 실패: " + e.getMessage());
        }
    }

    @DeleteMapping("/user-manager/profile/photo")
    public ResponseEntity<String> deleteUserProfile(@RequestParam("userId") Long userId) {
        try {
            userProfileService.removeProfilePhoto(userId);
            return ResponseEntity.ok("Profile photo removed successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(404)
                    .body("프로필 사진 삭제 실패: " + e.getMessage());
        }
    }

    @GetMapping("/user-manager/profile/photo")
    public ResponseEntity<String> getUserProfilePhoto(@RequestParam("userId") Long userId) {
        try {
            Long photoId = userProfileService.getProfilePhoto(userId);
            return ResponseEntity.ok(photoId.toString());
        } catch (Exception e) {
            return ResponseEntity
                    .status(404)
                    .body("프로필 사진 식별번호 조회 실패: " + e.getMessage());
        }
    }
}
