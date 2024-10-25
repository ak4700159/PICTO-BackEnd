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
public class TagSelectId implements java.io.Serializable {
    private static final long serialVersionUID = -5677594631567591053L;
    @Column(name = "tag", nullable = false, length = 20)
    private String tag;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TagSelectId entity = (TagSelectId) o;
        return Objects.equals(this.tag, entity.tag) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, userId);
    }

}