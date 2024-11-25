package picto.com.usermanager.domain.user.entity;

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
public class BlockId implements java.io.Serializable {
    private static final long serialVersionUID = 3256584913883115822L;
    @Column(name = "blocking_id", nullable = false)
    private Long blockingId;

    @Column(name = "blocked_id", nullable = false)
    private Long blockedId;

    public BlockId(Long sourceId, Long targetId) {
        this.blockingId = sourceId;
        this.blockedId = targetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BlockId entity = (BlockId) o;
        return Objects.equals(this.blockingId, entity.blockingId) &&
                Objects.equals(this.blockedId, entity.blockedId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockingId, blockedId);
    }

}