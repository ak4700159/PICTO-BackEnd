package picto.com.photomanager.global.postDomain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.domain.photo.entity.PhotoId;

import java.util.Objects;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class PhotoRecordId implements java.io.Serializable {
    private static final long serialVersionUID = 4786054818538112941L;
    private Long agentId;
    private PhotoId photoId;

    //@Column(name = "photo_id", nullable = false)
}