package picto.com.sessionscheduler.domain.session.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class ChattingMsgId {
    @Column(name = "chatting_id", nullable = false)
    private Long chatId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    private FolderId folderId;

    public ChattingMsgId(Long chatId, Long senderId, FolderId folderId) {
        this.chatId = chatId;
        this.folderId = folderId;
        this.senderId = senderId;
    }
}
