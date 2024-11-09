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
public class SharingFolderId implements java.io.Serializable {
    private static final long serialVersionUID = 7837330016239626474L;
    @Column(name = "sharing_folder_id", nullable = false)
    private Integer sharingFolderId;

    @Column(name = "link", nullable = false, length = 30)
    private String link;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SharingFolderId entity = (SharingFolderId) o;
        return Objects.equals(this.sharingFolderId, entity.sharingFolderId) &&
                Objects.equals(this.link, entity.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sharingFolderId, link);
    }
}