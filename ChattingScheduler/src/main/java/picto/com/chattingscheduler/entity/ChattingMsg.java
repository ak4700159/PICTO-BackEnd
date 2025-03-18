package picto.com.chattingscheduler.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ChattingMsg", schema = "photo_schema")
public class ChattingMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatting_id")
    private Long chattingId;

    @Column(name = "content", length = 100)
    private String content;

    @Column(name = "send_datetime", nullable = false)
    private long sendDatetime;

    @Column(name = "folder_id", nullable = false)
    private Long folderId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "generator_id", nullable = false)
    private Long generatorId;

    @MapsId("folderId")
    @ManyToOne(cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "folder_id", nullable = false, referencedColumnName = "folder_id")
    private Folder folder;

    @MapsId("senderId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sender_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    @Builder
    public ChattingMsg(String content, Folder folder, User user, long sendDatetime) {
        this.folderId = folder.getFolderId();
        this.senderId = user.getUserId();
        this.generatorId = folder.getGeneratorId();
        this.content = content;
        this.sendDatetime = sendDatetime;
        this.user = user;
        this.folder = folder;
    }

    public static ChattingMsg create(String content, Folder folder, User user) {
        return ChattingMsg.builder()
                .content(content)
                .folder(folder)
                .user(user)
                .build();
    }
}
