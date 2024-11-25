package picto.com.usermanager.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagRequest {
    Long userId;
    List<String> tagNames;
}
