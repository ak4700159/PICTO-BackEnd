package picto.com.chattingscheduler.domain.session.restapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import picto.com.chattingscheduler.domain.session.application.ChattingService;
import picto.com.chattingscheduler.domain.session.dto.ChatMsg;
import picto.com.chattingscheduler.domain.session.dto.request.DeleteChatMsgReq;
import picto.com.chattingscheduler.domain.session.dto.request.GetChattingMsgRequest;
import picto.com.chattingscheduler.domain.session.dto.response.GetChattingMsgResponse;
import picto.com.chattingscheduler.domain.session.entity.ChattingMsg;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Controller
public class ChattingController {
    private final ChattingService chattingService;

    @MessageMapping("/chat/enter")
    public void enter(ChatMsg message) {
        message.setSendDatetime(System.currentTimeMillis());
        chattingService.enterChatFolder(message);
    }


    @MessageMapping("/chat/exit")
    public void exit(ChatMsg message) {
        message.setSendDatetime(System.currentTimeMillis());
        chattingService.leaveChatFolder(message);
    }

    // 클라이언트에서 send/chat/message 경로로 메시지 발행시 실행
    @MessageMapping("chat/message")
    public void message(ChatMsg message) {
        // 수신한 메시지를 지정된 토픽으로 BroadCasting하는 기능을 수행
        // MessageType 타입에 따라 여러 로직으로 분기 가능
        System.out.println("===== 채팅 내역 ======\n" + message.toString());
        System.out.println("현재 접속자 : " + chattingService.getFolderChatMembers(message.getFolderId()));
        ChatMsg.MessageType msgType = message.getMessageType();
        message.setSendDatetime(System.currentTimeMillis());
        if(msgType == ChatMsg.MessageType.TALK)
            // /folder/{folderId}로 구독 중인 모든 클라이언트에게 메시지를 보낸다.
            chattingService.sendMessageToFolder(message);
    }
}