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
@Table(name = "ChattingMsg", schema = "photo_schema")
public class ChattingMsg {
    @EmbeddedId
    private ChattingMsgId chattingMsgId;

    @Column(name = "content", length = 100)
    private String content;

    @Column(name = "send_datetime")
    private Long sendDatetime;

    @MapsId("folderId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumns({
            @JoinColumn(name = "generator_id", nullable = false, referencedColumnName = "generator_id"),
            @JoinColumn(name = "folder_id", nullable = false, referencedColumnName = "folder_id")
    })
    private Folder folder;

    @MapsId("senderId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sender_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    @Builder
    public ChattingMsg(String content, Folder folder, User user, ChattingMsgId chattingMsgId) {
        this.content = content;
        this.sendDatetime =  new Date().getTime();
        this.user = user;
        this.folder = folder;
        this.chattingMsgId = chattingMsgId;
    }

    public static ChattingMsg create(String content, Folder folder, User user, ChattingMsgId chattingMsgId) {
        return ChattingMsg.builder()
                .content(content)
                .folder(folder)
                .user(user)
                .chattingMsgId(chattingMsgId)
                .build();
    }
}
