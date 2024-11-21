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

    @MessageMapping("/chat/exit")
    public void enter(ChatMsg message) {
        chattingService.enterChatFolder(message.getFolderId(), message.getSenderId());
        /* 입장 메시지를 다른 사람들에게 전송
        message.setContent(message.getFolderId() + "님이 입장하셨습니다.");
        chattingService.sendMessageToFolderExceptSender(
                message.getFolderId(),
                message.getSenderId(),
                message
        );
        */
    }

    // 클라이언트에서 send/chat/message 경로로 메시지 발행시 실행
    @MessageMapping("chat/message")
    public void message(ChatMsg message) {
        // 수신한 메시지를 지정된 토픽으로 BroadCasting하는 기능을 수행
        // MessageType 타입에 따라 여러 로직으로 분기 가능
        System.out.println("===== 채팅 내역 ======\n" + message.toString());
        System.out.println("현재 접속자 : " + chattingService.getFolderChatMembers(message.getFolderId()));
        ChatMsg.MessageType msgType = message.getMessageType();
        if(msgType == ChatMsg.MessageType.TALK)
            // /folder/{folderId}로 구독 중인 모든 클라이언트에게 메시지를 보낸다.
            chattingService.sendMessageToFolde(message.getFolderId(), message.getSenderId(), message);
    }

    // 채팅방 퇴장
    @MessageMapping("/chat/leave")
    public void leave(ChatMsg message) {
        chattingService.leaveChatFolder(message.getFolderId(), message.getSenderId());
    }

    @GetMapping("/chatting-scheduler/folders/{folderId}/chatters")
    @ResponseBody
    public ResponseEntity<Set<Long>> getFolderMembers(@PathVariable Long folderId) {
        Set<Long> members = chattingService.getFolderChatMembers(folderId);
        return ResponseEntity.ok().body(members);
    }

    // 특정 폴더 채팅 기록 조회
    @GetMapping("/chatting-scheduler/folders/{folderId}/chat")
    @ResponseBody
    public ResponseEntity<List<GetChattingMsgResponse>> getFolderChatMembers(@PathVariable Long folderId) {
        List<ChattingMsg> chattingMsgs = chattingService.getFolderChatMessages(folderId);

        List<GetChattingMsgResponse> chattingMsgResponses = chattingMsgs
                .stream()
                .map(GetChattingMsgResponse::new)
                .toList();
            return ResponseEntity.ok().body(chattingMsgResponses);
    }

    // 특정 사용자 채팅 기록 조회
    @GetMapping("/chatting-scheduler/users/chat")
    @ResponseBody
    public ResponseEntity<List<GetChattingMsgResponse>> getSenderChatMembers(@RequestBody GetChattingMsgRequest request){
        List<ChattingMsg> chattingMsgs;
        if(request.getSenderId() != null && request.getFolderId() != null) {
            chattingMsgs = chattingService.getSenderFolderChatMessages(request.getFolderId(), request.getSenderId());
        }
        else{
            chattingMsgs = chattingService.getSenderChatMessages(request.getSenderId());
        }
        List<GetChattingMsgResponse> chattingMsgResponses = chattingMsgs
                .stream()
                .map(GetChattingMsgResponse::new)
                .toList();
        return ResponseEntity.ok().body(chattingMsgResponses);
    }

    @DeleteMapping("/chatting-scheduler/chat")
    @ResponseBody
    public ResponseEntity<?> deleteChatMsg(@RequestBody DeleteChatMsgReq request) {
        chattingService.deleteChatMsg(request);
        return ResponseEntity.ok().body(null);
    }
}