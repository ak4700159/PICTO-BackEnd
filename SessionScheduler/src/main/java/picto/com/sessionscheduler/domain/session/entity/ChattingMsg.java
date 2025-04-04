package picto.com.sessionscheduler.domain.session.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ChattingMsg", schema = "picto_schema")
public class ChattingMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatting_id")
    private Long chattingId;

    @Column(name = "content", length = 100)
    private String content;

    @Column(name = "send_datetime")
    private Long sendDatetime;

    @Column(name = "folder_id", nullable = false)
    private Long folderId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @MapsId("folderId")
    @ManyToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "folder_id", nullable = false, referencedColumnName = "folder_id")
    private Folder folder;

    @MapsId("senderId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sender_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    @Builder
    public ChattingMsg(Long folderId, Long senderId, String content, Folder folder, User user) {
        this.folderId = folderId;
        this.senderId = senderId;
        this.content = content;
        this.sendDatetime =  new Date().getTime();
        this.user = user;
        this.folder = folder;
    }

    public static ChattingMsg create(String content, Folder folder, User user) {
        return ChattingMsg.builder()
                .folderId(folder.getFolderId())
                .senderId(user.getUserId())
                .content(content)
                .folder(folder)
                .user(user)
                .build();
    }
}
