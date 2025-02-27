package picto.com.sessionscheduler.domain.session.application;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picto.com.sessionscheduler.domain.session.dto.SessionInfo;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Component
public class UserSessionRegistry {
    // 현재 접속 중인 사용자 목록 반환
    // 접속 중인 사용자 목록 (세션 ID -> 사용자명)
    private final List<SessionInfo> activeUsers = new CopyOnWriteArrayList<>();

    // 사용자 추가
    public void addUser(SessionInfo newUser) {
        if(!activeUsers.contains(newUser)) {
            activeUsers.add(newUser);
            System.out.println("[INFO] Adding server memory");
        } else {
            System.out.println("[WARN] User already exists");
        }
    }

    // 사용자 제거
    public void removeUser(String sessionId) {
        activeUsers.removeIf(sessionInfo -> sessionInfo.getSessionId().equals(sessionId));
    }

}
