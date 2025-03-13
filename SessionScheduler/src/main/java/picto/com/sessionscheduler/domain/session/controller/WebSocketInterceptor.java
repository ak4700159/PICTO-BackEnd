package picto.com.sessionscheduler.domain.session.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import picto.com.sessionscheduler.domain.session.application.UserSessionRegistry;
import picto.com.sessionscheduler.domain.session.dto.SessionInfo;

@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {
    private final UserSessionRegistry registry;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(message);

        // Flutter client에서 보낸 "User-Id" 헤더를 추출
        String userId = accessor.getFirstNativeHeader("User-Id");
        String sessionId = accessor.getSessionId();

        if (userId != null) {
            // 세션에 "userId" 저장
            accessor.getSessionAttributes().put("userId", userId);
            System.out.println("[INFO] WebSocket Connected - User-Id: " + userId);
            System.out.println("[INFO] WebSocket Connected - Session-Id: " + sessionId);
            registry.addUser(new SessionInfo(sessionId, Long.parseLong(userId), System.currentTimeMillis()));
        }
        return message;
    }

}
