package picto.com.photomanager.global.postDomain.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveId implements java.io.Serializable {
    private static final long serialVersionUID = -5321873869717584258L;

    // 복합키 선언만 하면 된다.
    // Save Entity 에서 JoinColumn으로 사용하는 외래키 지정
    private Long photoId;
    private Long folderId;

    public SaveId(Long photoId, Long folderId) {
        this.photoId = photoId;
        this.folderId = folderId;
    }
}