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
public class SaveId implements java.io.Serializable {
    private static final long serialVersionUID = 7764666877134065055L;
    @Column(name = "photo_path", nullable = false, length = 50)
    private String photoPath;

    @Column(name = "sharing_folder_id", nullable = false)
    private Integer sharingFolderId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SaveId entity = (SaveId) o;
        return Objects.equals(this.photoPath, entity.photoPath) &&
                Objects.equals(this.sharingFolderId, entity.sharingFolderId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoPath, sharingFolderId, userId);
    }

}