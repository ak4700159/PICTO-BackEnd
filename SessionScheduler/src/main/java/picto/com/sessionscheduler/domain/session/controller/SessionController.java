package picto.com.sessionscheduler.domain.session.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import picto.com.sessionscheduler.domain.session.application.SessionService;
import picto.com.sessionscheduler.domain.session.dto.Message;

@RequiredArgsConstructor
@Controller
public class SessionController {
    private final SessionService sessionService;

    @MessageMapping("/session/enter")
    public void enter(Message message) {
        System.out.println("[enter] " + message.getSenderId());
        sessionService.enterSession(message.getSenderId());
    }

    @MessageMapping("/session/exit")
    public void leave(Message message) {
        System.out.println("[leave] " + message.getSenderId());
        sessionService.leaveSession(message.getSenderId());
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
        message.setSendDatetime(System.currentTimeMillis());
        if(message.getMessageType() == Message.MessageType.SHARE){
            sessionService.sharedPhoto(message);
        }
        else{
            throw new IllegalArgumentException("not valid message type");
        }
        return ResponseEntity.ok().body("ok");
    }
}
