package picto.com.photomanager.global.postDomain.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShareId {
    private Long userId;
    private Long folderId;

    public ShareId(Long userId, Long folderId) {
        this.userId = userId;
        this.folderId = folderId;
    }
}
