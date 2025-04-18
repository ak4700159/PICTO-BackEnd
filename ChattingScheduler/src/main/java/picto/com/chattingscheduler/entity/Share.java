package picto.com.chattingscheduler.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
