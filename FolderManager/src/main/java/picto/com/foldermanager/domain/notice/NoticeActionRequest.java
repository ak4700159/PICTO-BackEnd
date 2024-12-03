package picto.com.foldermanager.domain.notice;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeActionRequest {
    private Long receiverId;
    private Boolean accept;
}