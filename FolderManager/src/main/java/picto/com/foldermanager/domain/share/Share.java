package picto.com.foldermanager.domain.share;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.foldermanager.domain.folder.Folder;
import picto.com.foldermanager.domain.user.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Share")
public class Share {
    @EmbeddedId
    private ShareId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "folder_id", insertable = false, updatable = false)
    })
    private Folder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "shared_datetime", nullable = false)
    private Long sharedDatetime;

    @Builder
    public Share(ShareId id, Folder folder, User user) {
        this.id = id;
        this.folder = folder;
        this.user = user;
        this.sharedDatetime = System.currentTimeMillis();
    }
}
