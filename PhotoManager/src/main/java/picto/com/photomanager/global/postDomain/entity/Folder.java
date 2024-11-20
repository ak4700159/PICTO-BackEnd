package picto.com.photomanager.global.postDomain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.photomanager.domain.user.entity.User;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Folder {
    @EmbeddedId
    private FolderId id;

    @MapsId("generatorId")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "generator_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "created_datetime", nullable = false)
    private Long createdDatetime;

    @Column(name = "content", nullable = false, length = 50)
    private String content;

    @Column(name = "link", nullable = false, length = 30)
    private String link;

    @Builder
    public Folder(FolderId id, String name, Long createdDatetime, String content, String link, User user) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.createdDatetime = createdDatetime;
        this.content = content;
        this.link = link;
    }
}