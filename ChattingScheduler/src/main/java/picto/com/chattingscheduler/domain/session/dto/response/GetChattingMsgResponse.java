package picto.com.chattingscheduler.domain.session.dto.response;


import lombok.*;
import picto.com.chattingscheduler.domain.session.entity.ChattingMsg;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetChattingMsgResponse {
    private String content;
    private Long folderId;
    private Long senderId;
    private Long sendDatetime;

    public GetChattingMsgResponse(ChattingMsg chattingMsg) {
        this.content = chattingMsg.getContent();
        this.folderId = chattingMsg.getChattingMsgId().getFolderId().getFolderId();
        this.senderId = chattingMsg.getChattingMsgId().getSenderId();
        this.sendDatetime = chattingMsg.getSendDatetime();
    }
}
