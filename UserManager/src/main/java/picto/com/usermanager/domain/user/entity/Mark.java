package picto.com.usermanager.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mark {
    @EmbeddedId
    private MarkId id;

    @MapsId("markingId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "marking_id", nullable = false)
    private User marking;

    @MapsId("markedId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "marked_id", nullable = false)
    private User marked;

    public Mark(MarkId id, User targetUser, User sourceUser) {
        this.id = id;
        this.marked= targetUser;
        this.marking = sourceUser;
    }
}