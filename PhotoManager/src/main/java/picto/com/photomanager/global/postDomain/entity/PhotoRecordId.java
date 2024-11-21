package picto.com.photomanager.global.postDomain.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoRecordId implements java.io.Serializable {
    private static final long serialVersionUID = 4786054818538112941L;
    private Long agentId;
    private Long photoId;

    public PhotoRecordId(Long agentId, Long photoId) {
        this.agentId = agentId;
        this.photoId = photoId;
    }
}