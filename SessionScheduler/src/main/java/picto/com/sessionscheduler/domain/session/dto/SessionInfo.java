package picto.com.sessionscheduler.domain.session.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionInfo {
    private Long sessionId;
    private Long userId;
    private Long sessionStartTime;
}
