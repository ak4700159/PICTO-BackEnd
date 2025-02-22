package picto.com.sessionscheduler.domain.session.application;

import org.springframework.stereotype.Component;
import picto.com.sessionscheduler.domain.session.dto.SessionInfo;

import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class UserSessionRegistry {
    // 접속 중인 사용자 목록 (세션 ID -> 사용자명)
    private final ConcurrentSkipListSet<SessionInfo> activeUsers = new ConcurrentSkipListSet<SessionInfo>();

    // 사용자 추가
    public void addUser(SessionInfo newUser) {
        if(!activeUsers.contains(newUser)) {
            System.out.println("[INFO] Adding server memory");
            activeUsers.add(newUser);
        } else {
            System.out.println("[WARN] User already exists");
        }
    }

    // 사용자 제거
    public void removeUser(String sessionId) {
        activeUsers.removeIf(sessionInfo -> sessionInfo.getSessionId().equals(sessionId));
    }

    // 현재 접속 중인 사용자 목록 반환
    public ConcurrentSkipListSet<SessionInfo> getActiveUsers(Long roomId) {
        return activeUsers;
    }
}
