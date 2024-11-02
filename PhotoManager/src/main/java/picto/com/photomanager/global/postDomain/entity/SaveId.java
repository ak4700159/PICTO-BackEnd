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
public class SaveId implements java.io.Serializable {
    private static final long serialVersionUID = -5321873869717584258L;
    @Column(name = "photo_id", nullable = false)
    private Integer photoId;

    @Column(name = "sharing_folder_id", nullable = false)
    private Integer sharingFolderId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "link", nullable = false, length = 30)
    private String link;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SaveId entity = (SaveId) o;
        return Objects.equals(this.sharingFolderId, entity.sharingFolderId) &&
                Objects.equals(this.link, entity.link) &&
                Objects.equals(this.photoId, entity.photoId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sharingFolderId, link, photoId, userId);
    }

}