package picto.com.photomanager.global.postDomain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.domain.user.entity.User;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Share {
    @EmbeddedId
    private ShareId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @MapsId("folderId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "folder_id", referencedColumnName = "folder_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Folder folder;

    @Column(name = "shared_datetime", nullable = false)
    private Long sharedDatetime;

    @Builder
    public Share(User user, Folder folder, Long sharedDatetime, ShareId id) {
        this.id = id;
        this.user = user;
        this.folder = folder;
        this.sharedDatetime = sharedDatetime;
    }
}
