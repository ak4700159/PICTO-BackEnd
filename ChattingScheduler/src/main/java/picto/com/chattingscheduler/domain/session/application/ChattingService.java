package picto.com.chattingscheduler.domain.session.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import picto.com.chattingscheduler.domain.session.dao.ChattingMsgRepository;
import picto.com.chattingscheduler.domain.session.dao.FolderRepository;
import picto.com.chattingscheduler.domain.session.dao.ShareRepository;
import picto.com.chattingscheduler.domain.session.dao.UserRepository;
import picto.com.chattingscheduler.domain.session.dto.ChatMsg;
import picto.com.chattingscheduler.domain.session.dto.request.DeleteChatMsgReq;
import picto.com.chattingscheduler.domain.session.entity.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ChattingService {
    // hashMap {key = folderId : value = set(**UserId)}
    private final Map<Long, HashSet<Long>> folderChatMembers = new ConcurrentHashMap<>();
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChattingMsgRepository chattingMsgRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final ShareRepository shareRepository;

    // 구독중인 클라이언트는 전송받은 메시지가 자신의 아이디가 맞는지 비교하고
    // 자신의 아이디라면 에러 메시지에 대한 내용(TALK)인지 아니면 자신이 보낸 메시지인지 타입을 확인해서 예외처리 한다.

    // 채팅방 입장
    @Transactional
    public void enterChatFolder(ChatMsg message) {
        if(!verifiyChat(message)){
            return;
        }

        // 이전에 채팅방이 없었더라면 새로운 키값을 부여해 생성
        folderChatMembers.computeIfAbsent(message.getFolderId(), k -> new HashSet<Long>());

        // 채팅방에 이미 있는 경우
        if(folderChatMembers
                .get(message.getFolderId())
                .contains(message.getSenderId())) {
            message.setMessageType(ChatMsg.MessageType.DUPLICATED_SESSION);
            messagingTemplate.convertAndSend("/folder/" + message.getFolderId(), message);
            return;
        }

        folderChatMembers.get(message.getFolderId()).add(message.getSenderId());
        System.out.println("[enter] : " + message.getSenderId());
    }

    @Transactional
    public void leaveChatFolder(ChatMsg message) {
        if(!verifiyChat(message)){
            return;
        }

        Set<Long> users = folderChatMembers.get(message.getFolderId());
        if (users != null) {
            // 채팅에 접속 중인 사용자가 있어야 된다.
            if(!users.contains(message.getSenderId())) {
                message.setMessageType(ChatMsg.MessageType.NOT_FOUND_USER);
                messagingTemplate.convertAndSend("/folder/" + message.getFolderId(), message);
                return;
            }

            users.remove(message.getSenderId());
            // 채팅방 안에 사용자가 없다면 키삭제
            if (users.isEmpty()) {
                folderChatMembers.remove(message.getFolderId());
            }
        }
        // 채팅방이 존재하지 않는 경우
        else{
            message.setMessageType(ChatMsg.MessageType.NOT_FOUND_FOLDER);
            messagingTemplate.convertAndSend("/folder/" + message.getFolderId(), message);
        }
        System.out.println("[leave] : " + message.getSenderId());
    }

    // 특정 사용자를 제외한 채팅방 모든 사용자에게 메시지 전송
    @Transactional
    public void sendMessageToFolder(ChatMsg message) {
        if(!verifiyChat(message)) return;

        // 세션
        if (!folderChatMembers.containsKey(message.getFolderId())) {
            message.setMessageType(ChatMsg.MessageType.NOT_EXIST_SESSION);
            messagingTemplate.convertAndSend("/folder/" + message.getFolderId(), message);
            return;
        }
        else if(!folderChatMembers.get(message.getFolderId()).contains(message.getSenderId())){
            message.setMessageType(ChatMsg.MessageType.NOT_FOUND_USER);
            messagingTemplate.convertAndSend("/folder/" + message.getFolderId(), message);
            return;
        }

        // 메시지를 디비에 저장 -> 기록
        User sender = userRepository.getReferenceById(message.getSenderId());
        Folder folder = folderRepository.getReferenceById(message.getFolderId());
        chattingMsgRepository.save(message.toEntity(sender, folder));

        // 사용자들에게 전송
        messagingTemplate.convertAndSend("/folder/" + message.getFolderId(), message);
    }

    // 채팅 유효성 확인
    public boolean verifiyChat(ChatMsg message){
        // 폴더 존재 여부
        if(!folderRepository.existsById(message.getFolderId())) {
            message.setMessageType(ChatMsg.MessageType.NOT_FOUND_FOLDER);
            messagingTemplate.convertAndSend("/folder/" + message.getFolderId(), message);
            return false;
        }

        // 존해하는 사용자인지
        if(!userRepository.existsById(message.getSenderId())){
            message.setMessageType(ChatMsg.MessageType.NOT_FOUND_USER);
            messagingTemplate.convertAndSend("/folder/" + message.getFolderId(), message);
            return false;
        }

        // 공유 여부
        if(shareRepository.findByFolderId(message.getFolderId(), message.getSenderId()) == null){
            message.setMessageType(ChatMsg.MessageType.NOT_ALLOWED_USER);
            messagingTemplate.convertAndSend("/folder/" + message.getFolderId(), message);
            return false;
        }
        return true;
    }


    // REST CONTROLLER SERVICE LOGIC



    // 현재 채팅방의 멤버 목록 조회 -> userId 조회
    public Set<Long> getFolderChatMembers(Long folderId) {
        return folderChatMembers.getOrDefault(folderId, new HashSet<Long>());
    }

    // 폴더 채팅 전체 내역 조회
    public List<ChattingMsg> getFolderChatMessages(Long folderId) {
        List<ChattingMsg> chattingMsgs;
        chattingMsgs = chattingMsgRepository.findByfolderId(folderId);
        return chattingMsgs;
    }

    // 특정 사용자 전체 채팅 내역 조히
    public List<ChattingMsg> getSenderChatMessages(Long senderId) {
        List<ChattingMsg> chattingMsgs;
        chattingMsgs = chattingMsgRepository.findBySenderId(senderId);
        return chattingMsgs;
    }

    // 특정 폴더, 특정 사용자 채팅 내역 조회
    public List<ChattingMsg> getSenderFolderChatMessages(Long senderId, Long folderId) {
        List<ChattingMsg> chattingMsgs;
        chattingMsgs = chattingMsgRepository.findBySenderIdAndFolderId(senderId, folderId);
        return chattingMsgs;
    }

    // 채팅 삭제
    public void deleteChatMsg(DeleteChatMsgReq request) throws IllegalAccessException {
        ChattingMsg msg = chattingMsgRepository.getReferenceById(request.getChatMsgId());
        if(msg.getFolderId() != request.getFolderId() || msg.getSenderId() != request.getSenderId()){
            throw new IllegalAccessException("삭제할 수 없는 메시지");
        }

        chattingMsgRepository.delete(chattingMsgRepository.getReferenceById(request.getChatMsgId()));
    }
}
