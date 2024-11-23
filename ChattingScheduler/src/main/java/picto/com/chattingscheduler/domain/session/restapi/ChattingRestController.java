package picto.com.chattingscheduler.domain.session.restapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import picto.com.chattingscheduler.domain.session.application.ChattingService;
import picto.com.chattingscheduler.domain.session.dto.request.DeleteChatMsgReq;
import picto.com.chattingscheduler.domain.session.dto.request.GetChattingMsgRequest;
import picto.com.chattingscheduler.domain.session.dto.response.GetChattingMsgResponse;
import picto.com.chattingscheduler.domain.session.entity.ChattingMsg;
import picto.com.chattingscheduler.domain.session.entity.Folder;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class ChattingRestController {
    private final ChattingService chattingService;

    // 특정 폴더 채팅에 참여중인 사용자 리스트 조회
    @GetMapping("/chatting-scheduler/folder/users")
    ResponseEntity<Set<Long>> getFolderChattingUsers(@RequestBody Long folderId) {
        Set<Long> userList = chattingService.getFolderChatMembers(folderId);
        return ResponseEntity.ok().body(userList);
    }

    @GetMapping("/chatting-scheduler/folders/{folderId}/chatters")
    public ResponseEntity<Set<Long>> getFolderMembers(@PathVariable("folderId") Long folderId) {
        Set<Long> members = chattingService.getFolderChatMembers(folderId);
        return ResponseEntity.ok().body(members);
    }

    // 특정 폴더 채팅 기록 조회
    @GetMapping("/chatting-scheduler/folders/{folderId}/chat")
    public ResponseEntity<List<GetChattingMsgResponse>> getFolderChatMembers(@PathVariable("folderId") Long folderId) {
        List<ChattingMsg> chattingMsgs = chattingService.getFolderChatMessages(folderId);

        List<GetChattingMsgResponse> chattingMsgResponses = chattingMsgs
                .stream()
                .map(GetChattingMsgResponse::new)
                .toList();
        return ResponseEntity.ok().body(chattingMsgResponses);
    }

    // 특정 사용자 채팅 기록 조회
    @GetMapping("/chatting-scheduler/users/chat")
    public ResponseEntity<List<GetChattingMsgResponse>> getSenderChatMembers(@RequestBody GetChattingMsgRequest request){
        List<ChattingMsg> chattingMsgs;
        if(request.getSenderId() != null && request.getFolderId() != null) {
            chattingMsgs = chattingService.getSenderFolderChatMessages(request.getSenderId(), request.getFolderId());
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
    public ResponseEntity<?> deleteChatMsg(@RequestBody DeleteChatMsgReq request) {
        try{
            chattingService.deleteChatMsg(request);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(null);
    }

}
