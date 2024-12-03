package picto.com.photomanager.global.getDomain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MarkId implements java.io.Serializable {
    private static final long serialVersionUID = -691557134142325189L;
    @Column(name = "marking_id", nullable = false)
    private Long markingId;

    @Column(name = "marked_id", nullable = false)
    private Long markedId;

    public MarkId(Long sourceId, Long targetId) {
        this.markingId = sourceId;
        this.markedId = targetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MarkId entity = (MarkId) o;
        return Objects.equals(this.markingId, entity.markingId) &&
                Objects.equals(this.markedId, entity.markedId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(markingId, markedId);
    }

}