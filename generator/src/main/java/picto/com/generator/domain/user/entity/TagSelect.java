package picto.com.generator.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TagSelect", schema = "picto_schema", indexes = {
        @Index(name = "user_id", columnList = "user_id")
})
public class TagSelect {
    // 복합키를 사용했기 때문에
    @EmbeddedId
    private TagSelectId tagSelectedId;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    @Builder
    public TagSelect(TagSelectId tagSelectedId, User user) {
        this.tagSelectedId = tagSelectedId;
        this.user = user;
    }
}