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
public class ShareId implements java.io.Serializable {
    private static final long serialVersionUID = -4722965857382336147L;
    @Column(name = "link", nullable = false, length = 30)
    private String link;

    @Column(name = "sharing_folder_id", nullable = false)
    private Integer sharingFolderId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ShareId entity = (ShareId) o;
        return Objects.equals(this.sharingFolderId, entity.sharingFolderId) &&
                Objects.equals(this.link, entity.link) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sharingFolderId, link, userId);
    }

}