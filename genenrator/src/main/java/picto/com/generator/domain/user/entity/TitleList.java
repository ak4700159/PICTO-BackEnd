package picto.com.generator.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "TitleList", schema = "picto_schema", indexes = {
        @Index(name = "name", columnList = "name"),
        @Index(name = "user_id", columnList = "user_id")
})
public class TitleList {
    @Id
    @Column(name = "title_list_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long titleListId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "name", nullable = false, referencedColumnName = "name")
    private Title title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User user;
}