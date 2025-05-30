package picto.com.generator.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id", nullable = false)
    private Long folderId;

    @Column(name = "generator_id", nullable = false)
    private Long generatorId;

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

    @Override
    public String toString() {
        return "Folder [folderId=" + folderId + ", generatorId=" + generatorId + ", user=" + content + ", name=" + name + "]";
    }

    @Builder
    public Folder(Long folderId, Long generatorId, String name, Long createdDatetime, String content, User user) {
        this.folderId = folderId;
        this.generatorId = generatorId;
        this.user = user;
        this.name = name;
        this.createdDatetime = createdDatetime;
        this.content = content;
    }
}