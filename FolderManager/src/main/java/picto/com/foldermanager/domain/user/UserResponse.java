package picto.com.foldermanager.domain.user;

import lombok.Builder;
import lombok.Getter;
import picto.com.foldermanager.domain.folder.Folder;

@Getter
@Builder
public class UserResponse {
    private Long folderId;
    private Long userId;
    private String name;
    private String email;

    public static picto.com.foldermanager.domain.user.UserResponse from(User user, Folder folder) {
        return picto.com.foldermanager.domain.user.UserResponse.builder()
                .folderId(folder.getId())
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}