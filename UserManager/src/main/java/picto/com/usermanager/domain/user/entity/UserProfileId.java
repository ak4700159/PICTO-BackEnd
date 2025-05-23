package picto.com.usermanager.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class UserProfileId implements java.io.Serializable {
    private static final long serialVersionUID = 41580944295845720L;
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "photo_id", nullable = false)
    private Long photoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserProfileId entity = (UserProfileId) o;
        return Objects.equals(this.photoId, entity.photoId) &&
                Objects.equals(this.userId, entity.userId);
    }

    public UserProfileId(Long userId, Long photoId) {
        this.userId = userId;
        this.photoId = photoId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoId, userId);
    }

}