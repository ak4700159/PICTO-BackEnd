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
public class Block {
    @EmbeddedId
    private BlockId id;

    @MapsId("blockingId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "blocking_id", nullable = false)
    private User blocking;

    @MapsId("blockedId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "blocked_id", nullable = false)
    private User blocked;

    public Block(BlockId id, User sourceUser, User targetUser) {
        this.id = id;
        this.blocking = sourceUser;
        this.blocked = targetUser;
    }

}