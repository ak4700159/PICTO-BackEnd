package picto.com.generator.global.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import picto.com.generator.domain.photo.entity.PhotoId;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class PhotoRecordId implements java.io.Serializable {
    private static final long serialVersionUID = 4786054818538112941L;
    private Integer agentId;
    private PhotoId photoId;
}