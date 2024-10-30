package picto.com.generator.global.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.generator.domain.user.domain.User;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TagSelect", schema = "photo_schema", indexes = {
        @Index(name = "user_id", columnList = "user_id")
})
public class TagSelect {
    // 복합키를 사용했기 때문에
    @EmbeddedId
    private TagSelectId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public TagSelect(TagSelectId id, User user) {
        this.id = id;
        this.user = user;
    }
}