package picto.com.photomanager.global.postDomain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class PhotoRecordId implements java.io.Serializable {
    private static final long serialVersionUID = 4786054818538112941L;
    @Column(name = "agent_id", nullable = false)
    private Integer agentId;

    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;

    @Column(name = "photo_id", nullable = false)
    private Integer photoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PhotoRecordId entity = (PhotoRecordId) o;
        return Objects.equals(this.agentId, entity.agentId) &&
                Objects.equals(this.photoId, entity.photoId) &&
                Objects.equals(this.ownerId, entity.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentId, photoId, ownerId);
    }

}