package picto.com.generator.global.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.util.Objects;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class TagSelectId implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1985514141084711612L;
    @Column(name = "tag", nullable = false, length = 20)
    private String tag;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    public TagSelectId(String tag, Integer userId) {
        this.tag = tag;
        this.userId = userId;
    }

}