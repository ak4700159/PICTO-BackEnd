package picto.com.usermanager.domain.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picto.com.usermanager.domain.user.dao.PhotoRepository;
import picto.com.usermanager.domain.user.dao.UserProfileRepository;
import picto.com.usermanager.domain.user.dao.UserRepository;
import picto.com.usermanager.domain.user.entity.UserProfile;
import picto.com.usermanager.domain.user.entity.UserProfileId;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    // 1. 해당 사용자의 식별 번호가 있는지
    // 2. 해당 사진의 식별 번호가 있는지
    // 3. 해당 사진의 소유자의 식별 번호와 사용자의 식별 번호가 동일한지
    // 4. 위의 항목들을 만족하면 프로필 테이블을 업데이트
    // 5. 업데이트 하기 전 기존에 사용자 프로필이 있으면 삭제 후 업데이트 진행
    public void updateProfilePhoto(Long userId, Long photoId) {
        // 1. 사용자 존재 여부 확인
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        // 2. 사진 존재 여부 확인
        var photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사진이 존재하지 않습니다."));

        // 3. 사진의 소유자가 사용자와 일치하는지 확인
        if (!photo.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("사진의 소유자가 해당 사용자와 일치하지 않습니다.");
        }

        // 4. 기존 프로필 존재 시 삭제
        UserProfileId userProfileId = new UserProfileId(userId, photoId);
        userProfileRepository.findAll().stream()
                .filter(p -> p.getUser().getUserId().equals(userId))
                .findFirst()
                .ifPresent(userProfileRepository::delete);

        // 5. 새로운 프로필 저장
        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .photo(photo)
                .build();
        userProfileRepository.save(userProfile);
    }

    public void removeProfilePhoto(Long userId) {
        // 해당 유저의 프로필 존재 여부 확인 및 삭제
        userProfileRepository.findAll().stream()
                .filter(p -> p.getUser().getUserId().equals(userId))
                .findFirst()
                .ifPresent(userProfileRepository::delete);
    }

    public Long getProfilePhoto(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        return userProfileRepository.getUserProfileByUserId(userId);
    }
}
