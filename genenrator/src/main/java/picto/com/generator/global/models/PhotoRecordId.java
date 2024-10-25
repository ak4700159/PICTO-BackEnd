package picto.com.generator.global.models;

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
    private static final long serialVersionUID = 6630674680765074834L;
    @Column(name = "agent_id", nullable = false)
    private Integer agentId;

    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;

    @Column(name = "photo_path", nullable = false, length = 50)
    private String photoPath;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PhotoRecordId entity = (PhotoRecordId) o;
        return Objects.equals(this.agentId, entity.agentId) &&
                Objects.equals(this.photoPath, entity.photoPath) &&
                Objects.equals(this.ownerId, entity.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentId, photoPath, ownerId);
    }

}