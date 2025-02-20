package picto.com.sessionscheduler.domain.session.application;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class UserSessionRegistry {
    // 접속 중인 사용자 목록 (세션 ID -> 사용자명)
    private final ConcurrentSkipListSet<Long> activeUsers = new ConcurrentSkipListSet<>();

    // 사용자 추가
    public void addUser(Long userId) {
        if(!activeUsers.contains(userId)) {
            System.out.println("[INFO] Adding server memory");
            activeUsers.add(userId);
        } else {
            System.out.println("[WARN] User already exists");
        }
    }

    // 사용자 제거
    public void removeUser(Long userId) {
        activeUsers.remove(userId);
    }

    // 현재 접속 중인 사용자 목록 반환
    public ConcurrentSkipListSet<Long> getActiveUsers(Long roomId) {
        return activeUsers;
    }
}
