package picto.com.chattingscheduler.domain.session.dto;

import lombok.*;
import picto.com.chattingscheduler.domain.session.entity.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsg {
    // 메시지  타입 : 입장, 채팅, 퇴장
    public enum MessageType{
        ENTER, TALK, EXIT
    }

    // folderChatting 상황에서는 사용되지 않지만
    // Session 연결에서 중요하게 사용될 것으로 예상
    private MessageType messageType; // 메시지 타입
    private Long senderId;
    private Long folderId; // 폴더 식별키
    private String content; // 메시지
    private Long sendDatetime;

    @Override
    public String toString() {
        return "[senderId] : " + senderId +
                "\n[folderId] : " + folderId +
                "\nmessageType : " + messageType +
                "\nsendDatetime : " + sendDatetime +
                "\ncontent : " + content;
    }

    public ChattingMsg toEntity(User sender, Folder folder){
        return ChattingMsg
                .builder()
                .content(content)
                // FolderId (폴더 식별키, 폴더 생성자 식별키)
                // 수정 필요
                .chattingMsgId(new ChattingMsgId(Long.parseLong("1"), sender.getId(), folder.getId()))
                .user(sender)
                .folder(folder)
                .build();
    }
}