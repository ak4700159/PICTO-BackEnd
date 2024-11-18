package picto.com.photomanager.global.postDomain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.photomanager.domain.user.entity.User;

@Getter
@Setter
@Entity
public class Folder {
    @Id
    @Column(name = "generator_id", nullable = false)
    private int folderId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "generator_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "created_datetime", nullable = false)
    private Long savedDatetime;

    @Column(name = "content", nullable = false, length = 50)
    private String content;

    @Column(name = "link", nullable = false, length = 30)
    private String link;
}