package picto.com.chattingscheduler.domain.session.restapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import picto.com.chattingscheduler.domain.session.application.ChattingService;
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

    // 폴더 생성 (10)
    @PostMapping("/chatting-scheduler/folder")
    ResponseEntity<List<Folder>> createFolder() {
        List<Folder> folders =  chattingService.creteaFolder();
        return ResponseEntity.ok().body(folders);
    }
}
