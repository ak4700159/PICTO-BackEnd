package picto.com.genenrator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class TagId implements java.io.Serializable {
    private static final long serialVersionUID = 7501446422339556648L;
    @Column(name = "tag", nullable = false, length = 20)
    private String tag;

    @Column(name = "category", nullable = false, length = 20)
    private String category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TagId entity = (TagId) o;
        return Objects.equals(this.tag, entity.tag) &&
                Objects.equals(this.category, entity.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, category);
    }

}