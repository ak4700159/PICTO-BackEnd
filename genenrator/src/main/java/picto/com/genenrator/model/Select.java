package picto.com.genenrator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.genenrator.domain.user.domain.User;

@Getter
@Setter
@Entity
@Table(name = "`Select`")
public class Select {
    @EmbeddedId
    private SelectId id;

    @MapsId("tag")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "tag", nullable = false, referencedColumnName = "category")
    private Tag tag;

    @MapsId("category")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "category", nullable = false, referencedColumnName = "category")
    private Tag category;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}