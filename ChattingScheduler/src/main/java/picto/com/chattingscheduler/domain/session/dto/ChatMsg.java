package picto.com.chattingscheduler.domain.session.dto;

import lombok.*;
import picto.com.chattingscheduler.domain.session.entity.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsg {
    // 메시지 타입 : 입장, 채팅, 퇴장, 웹소켓 중복, 존재하지 않는 사용자, 존재하지 않는 폴더, 채팅을 허용할 수 없는 사용자
    public enum MessageType{
        ENTER, TALK, EXIT
        ,DUPLICATED_SESSION
        ,ALREADY_EXIST_SESSION
        ,NOT_EXIST_SESSION
        ,NOT_FOUND_USER
        ,NOT_ALLOWED_USER
        ,NOT_FOUND_FOLDER
    }

    private MessageType messageType;    // 채팅의 상태
    private Long senderId;              // 전송자 식별키
    private Long folderId;              // 폴더 식별키
    private String content;             // 채팅 내용
    private Long sendDatetime;          // 채팅 보낸 시점

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
                .sendDatetime(sendDatetime)
                .content(content)
                .user(sender)
                .folder(folder)
                .build();
    }
}