package picto.com.chattingscheduler.domain.session.application;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import picto.com.chattingscheduler.domain.session.dao.ChattingMsgRepository;
import picto.com.chattingscheduler.domain.session.dao.FolderRepository;
import picto.com.chattingscheduler.domain.session.dao.UserRepository;
import picto.com.chattingscheduler.domain.session.dto.ChatMsg;
import picto.com.chattingscheduler.domain.session.dto.request.DeleteChatMsgReq;
import picto.com.chattingscheduler.domain.session.entity.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ChattingService {
    // hashMap ky = folderId : value = set(UserId)
    private final Map<Long, Set<Long>> folderChatMembers = new ConcurrentHashMap<>();
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChattingMsgRepository chattingMsgRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    // 임의 폴더 생성
    public List<Folder> creteaFolder(){
        final int MAX_FOLDERS = 10;
        List<Folder> folders = new ArrayList<>();
        for(int i = 1; i <= MAX_FOLDERS; i++){
            Folder newFolder = folderRepository.save(Folder.builder().build());
            folders.add(newFolder);
        }
        return folders;
    }

    // 채팅방 입장
    public void enterChatFolder(Long folderId, Long userId) {
        // 이전에 채팅방이 없었더라면 새로운 키값을 부여해 생성
        folderChatMembers.computeIfAbsent(folderId, k -> ConcurrentHashMap.newKeySet()).add(userId);
    }

    // 채팅방 퇴장
    public void leaveChatFolder(Long folderId, Long userId) {
        Set<Long> users = folderChatMembers.get(folderId);

        if (users != null) {
            users.remove(userId);
            // 채팅방 안에 사용자가 없다면 키삭제
            if (users.isEmpty()) {
                folderChatMembers.remove(folderId);
            }
        }
    }

    // 특정 사용자를 제외한 채팅방 모든 사용자에게 메시지 전송
    public void sendMessageToFolde(Long folderId, Long senderId, ChatMsg message) {
        // 메시지를 디비에 저장 -> 기록
        User sender = userRepository.getReferenceById(senderId);
        Folder folder = folderRepository.getReferenceById(new FolderId(senderId, folderId));
        ChattingMsg newMsg = chattingMsgRepository.save(message.toEntity(sender, folder));

        // 사용자들에게 전송
        messagingTemplate.convertAndSend("/folder/" + folderId, message);
    }

    // 현재 채팅방의 멤버 목록 조회 -> userId 조회
    public Set<Long> getFolderChatMembers(Long folderId) {
        return folderChatMembers.getOrDefault(folderId, Collections.emptySet());
    }

    public List<ChattingMsg> getFolderChatMessages(Long folderId) {
        List<ChattingMsg> chattingMsgs;
        chattingMsgs = chattingMsgRepository.findByfolderId(folderId);
        return chattingMsgs;
    }

    public List<ChattingMsg> getSenderChatMessages(Long senderId) {
        List<ChattingMsg> chattingMsgs;
        chattingMsgs = chattingMsgRepository.findBySenderId(senderId);
        return chattingMsgs;
    }

    public List<ChattingMsg> getSenderFolderChatMessages(Long senderId, Long folderId) {
        List<ChattingMsg> chattingMsgs;
        chattingMsgs = chattingMsgRepository.findBySenderIdAndFolderId(senderId, folderId);
        return chattingMsgs;
    }

    public void deleteChatMsg(DeleteChatMsgReq request) {
        ChattingMsgId msgId =
                new ChattingMsgId(request.getChatId(), request.getSenderId(), new FolderId(request.getFolderId(), request.getGeneratorId()));
        chattingMsgRepository.delete(chattingMsgRepository.getReferenceById(msgId));
    }
}
