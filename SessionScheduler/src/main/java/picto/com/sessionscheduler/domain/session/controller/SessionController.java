package picto.com.sessionscheduler.domain.session.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import picto.com.sessionscheduler.domain.session.application.SessionService;
import picto.com.sessionscheduler.domain.session.application.UserSessionRegistry;
import picto.com.sessionscheduler.domain.session.dto.Message;

import java.util.Objects;

@RequiredArgsConstructor
@Controller
public class SessionController {
    private final SessionService sessionService;
    private final UserSessionRegistry registry;

    // 사용자가 웹소켓에서 나갈 때 실행됨
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = Objects.requireNonNull(headerAccessor.getSessionId());

        registry.removeUser(sessionId);
        System.out.printf("[INFO] %s USER leave\n", sessionId);
    }

    @MessageMapping("/session/location")
    public void message(Message message) {
        message.setSendDatetime(System.currentTimeMillis());
        if(message.getMessageType() == Message.MessageType.LOCATION)
            sessionService.receivedLocation(message);
        else{
            throw new IllegalArgumentException("not Invalid message type");
        }
    }

    @PostMapping("/session-scheduler/shared")
    @ResponseBody
    ResponseEntity<String> shared(@RequestBody Message message) {
        System.out.println("check");
        message.setSendDatetime(System.currentTimeMillis());
        if(message.getMessageType() == Message.MessageType.SHARE){
            sessionService.sharedPhoto(message, registry);
        }
        else{
            throw new IllegalArgumentException("not valid message type");
        }
        return ResponseEntity.ok().body("ok");
    }
}
