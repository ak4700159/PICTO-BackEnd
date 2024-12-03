package picto.com.foldermanager.domain.share;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShareRequest {
    private Long senderId;
    private Long receiverId;
    private Long folderId;
}