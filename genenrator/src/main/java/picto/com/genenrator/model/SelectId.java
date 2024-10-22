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
public class SelectId implements java.io.Serializable {
    private static final long serialVersionUID = -2300360966310438511L;
    @Column(name = "tag", nullable = false, length = 20)
    private String tag;

    @Column(name = "category", nullable = false, length = 20)
    private String category;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SelectId entity = (SelectId) o;
        return Objects.equals(this.tag, entity.tag) &&
                Objects.equals(this.category, entity.category) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, category, userId);
    }

}