package picto.com.chattingscheduler.domain.session.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Folder {
    @EmbeddedId
    private FolderId id;

    @MapsId("generatorId")
    @JoinColumn(name = "generator_id", nullable = false, referencedColumnName = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "created_datetime", nullable = false)
    private Long savedDatetime;

    @Column(name = "content", nullable = false, length = 50)
    private String content;

    @Builder
    public Folder(FolderId id, String name, String content, Long savedDatetime) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.savedDatetime = savedDatetime;
    }
}