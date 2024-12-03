package picto.com.foldermanager.domain.notice;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeResponse {
    private Long id;
    private NoticeType type;
    private Long senderId;
    private Long receiverId;
    private Long folderId;
    private String folderName;
    private Long createdDatetime;

    public static picto.com.foldermanager.domain.notice.NoticeResponse from(Notice notice) {
        return picto.com.foldermanager.domain.notice.NoticeResponse.builder()
                .id(notice.getId())
                .type(notice.getType())
                .senderId(notice.getSender().getId())
                .receiverId(notice.getReceiver().getId())
                .folderId(notice.getFolder().getId())
                .folderName(notice.getFolder().getName())
                .createdDatetime(notice.getCreatedDatetime())
                .build();
    }
}