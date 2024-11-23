package picto.com.chattingscheduler.domain.session.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteChatMsgReq {
    private Long senderId;
    private Long folderId;
    private Long chatMsgId;
}
