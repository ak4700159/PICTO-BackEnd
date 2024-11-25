package picto.com.usermanager.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventRequest {
    // MAKR(즐겨찾기) OR BLOCK(차단)
    // String type;
    Long sourceId;
    Long targetId;
}
