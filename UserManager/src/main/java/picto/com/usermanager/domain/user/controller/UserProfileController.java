package picto.com.usermanager.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import picto.com.usermanager.domain.user.application.UserProfileService;

@RestController
public class UserProfileController {
    private UserProfileService userProfileService;

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
}
