package picto.com.sessionscheduler.domain.session.dto;

import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
public class Message {
    // 메시지  타입 : 입장, 퇴장
    // LOCATION = 사용자 위치정보 (클라이언트 -> 서버)
    // SHARE = 공유 알림 (서버 -> 클라이언트)
    public enum MessageType{
        ENTER, LOCATION, EXIT, SHARE, DUPLICATED_SESSION, NOT_FOUND_USER, NO_EXIST_SESSION
    }

    // folderChatting 상황에서는 사용되지 않지만
    // Session 연결에서 중요하게 사용될 것으로 예상
    private MessageType messageType; // 메시지 타입
    private Long senderId;
    private Long photoId;
    private double lat;
    private double lng;
    private Long sendDatetime;

    @Builder
    public Message(MessageType type, Long senderId, Long photoId, double lat, double lng, Long sendDatetime) {
        this.messageType = type;
        this.senderId = senderId;
        this.photoId = photoId;
        this.lat = lat;
        this.lng = lng;
        this.sendDatetime = sendDatetime;
    }

    static public Message errorMessage(MessageType type){
        return Message
                .builder()
                .messageType(type)
                .build();
    }

    @Override
    public String toString() {
        return "[senderId] : " + senderId +
                "\nmessageType : " + messageType +
                "\nsendDatetime : " + sendDatetime +
                "\ncontent : " + photoId + "/" +  lat + "/" + lng;
    }
}