package picto.com.foldermanager.domain.notice;

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
@Table(name = "Notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoticeType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Column(name = "created_datetime", nullable = false)
    private Long createdDatetime;

    @Builder
    public Notice(NoticeType type, User sender, User receiver, Folder folder) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.folder = folder;
        this.createdDatetime = System.currentTimeMillis();
    }
}
