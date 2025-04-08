package picto.com.chattingscheduler.dto;

public record ChatMessageResponse(Long sendDatetime, String accountName, Long userId, String content) {
}
