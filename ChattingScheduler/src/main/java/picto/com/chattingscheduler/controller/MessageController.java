package picto.com.chattingscheduler.controller;

import picto.com.chattingscheduler.dao.UserRepository;
import picto.com.chattingscheduler.dao.ChattingMsgRepository;
import picto.com.chattingscheduler.dao.FolderRepository;
import picto.com.chattingscheduler.dto.ChatMessageRequest;
import picto.com.chattingscheduler.dto.ChatMessageResponse;
import picto.com.chattingscheduler.entity.User;
import picto.com.chattingscheduler.entity.ChattingMsg;
import picto.com.chattingscheduler.entity.Folder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@RestController
public class MessageController {

        private static final Logger log = LoggerFactory.getLogger(MessageController.class);
        private final UserRepository userRepository;
        private final ChattingMsgRepository chattingMsgRepository;
        private final FolderRepository folderRepository;

        public MessageController(UserRepository userRepository, ChattingMsgRepository chattingMsgRepository,
                        FolderRepository folderRepository) {
                this.userRepository = userRepository;
                this.chattingMsgRepository = chattingMsgRepository;
                this.folderRepository = folderRepository;
        }

        // 채팅 API
        // 예외처리 및 DB에 저장 후
        // "시간 이름: 채팅" 형식으로 변환 후 방송
        @Transactional
        @MessageMapping("/chat.{folderId}")
        @SendTo("/subscribe/chat.{folderId}")
        public ChatMessageResponse sendMessage(ChatMessageRequest request, @DestinationVariable Long folderId) {
                User user = userRepository.findById(request.senderId())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Folder folder = folderRepository.findById(folderId)
                                .orElseThrow(() -> new RuntimeException("Folder not found"));

                long sendDatetime = Instant.now().getEpochSecond();

                ChattingMsg message = ChattingMsg.builder()
                                .content(request.content())
                                .folder(folder)
                                .user(user)
                                .sendDatetime(sendDatetime)
                                .build();
                chattingMsgRepository.save(message);

                return new ChatMessageResponse(sendDatetime, user.getAccountName(), user.getUserId(),
                                request.content());
        }

        // 채팅내역 조회 함수 (어제부터 현재까지)
        public List<ChattingMsg> getPastMessages(Long folderId) {
                long startOfYesterday = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1)
                                .atStartOfDay(ZoneId.of("Asia/Seoul")).toEpochSecond();
                long now = Instant.now().getEpochSecond();
                return chattingMsgRepository.findByFolderIdAndSendDatetimeBetween(folderId, startOfYesterday, now);
        }

        // 예외처리
        @MessageExceptionHandler
        public void handleException(RuntimeException e) {
                log.info("Exception: {}", e.getMessage());
        }

        // 채팅내역 조회 API 매핑
        // 메시지 정보 바탕으로 채팅을 "시간 이름: 채팅" 형식으로 변환 후 반환
        @GetMapping("/chat/history/{folderId}")
        public List<ChatMessageResponse> getChatHistory(@PathVariable Long folderId) {
                return getPastMessages(folderId).stream()
                                .map(msg -> {
                                        return new ChatMessageResponse(msg.getSendDatetime(),
                                                        msg.getUser().getAccountName(),
                                                        msg.getSenderId(), msg.getContent());
                                })
                                .collect(Collectors.toList());
        }

        @GetMapping("/chat/folder/{folderId}")
        public List<ChatMessageResponse> getMessagesByFolderId(@PathVariable Long folderId) {
                return chattingMsgRepository.findByfolderId(folderId).stream()
                                .map(msg -> {
                                        return new ChatMessageResponse(msg.getSendDatetime(),
                                                        msg.getUser().getAccountName(),
                                                        msg.getSenderId(), msg.getContent());
                                })
                                .collect(Collectors.toList());
        }

        @GetMapping("/chat/sender/{senderId}")
        public List<ChatMessageResponse> getMessagesBySenderId(@PathVariable Long senderId) {
                return chattingMsgRepository.findBySenderId(senderId).stream()
                                .map(msg -> {
                                        return new ChatMessageResponse(msg.getSendDatetime(),
                                                        msg.getUser().getAccountName(),
                                                        msg.getSenderId(), msg.getContent());
                                })
                                .collect(Collectors.toList());
        }

        @GetMapping("/chat/folder/{folderId}/sender/{senderId}")
        public List<ChatMessageResponse> getMessagesByFolderIdAndSenderId(@PathVariable Long folderId,
                        @PathVariable Long senderId) {
                return chattingMsgRepository.findByFolderIdAndSenderId(folderId, senderId).stream()
                                .map(msg -> {
                                        return new ChatMessageResponse(msg.getSendDatetime(),
                                                        msg.getUser().getAccountName(),
                                                        msg.getSenderId(), msg.getContent());
                                })
                                .collect(Collectors.toList());
        }

        // 삭제 API
        // 삭제 요청한 유저가 채팅 송신자이거나 폴더 생성자인 경우 삭제 가능
        @Transactional
        @MessageMapping("/chat/delete/{folderId}/{messageId}")
        @SendTo("/subscribe/chat.{folderId}")
        public String deleteMessage(@DestinationVariable Long folderId, @DestinationVariable Long messageId,
                        ChatMessageRequest request) {
                ChattingMsg message = chattingMsgRepository.findById(messageId)
                                .orElseThrow(() -> new RuntimeException("Message not found"));

                if (!message.getUser().getUserId().equals(request.senderId())
                                && !message.getFolder().getGeneratorId().equals(request.senderId())) {
                        throw new RuntimeException("권한이 없습니다");
                }

                chattingMsgRepository.delete(message);
                return "Message deleted: " + messageId;
        }
}
