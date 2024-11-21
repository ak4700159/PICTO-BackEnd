package picto.com.chattingscheduler.domain.session.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetChattingMsgRequest {
    private Long senderId;
    private Long folderId;
}
