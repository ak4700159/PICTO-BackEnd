package picto.com.usermanager.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FilterRequest {
    Long userId;
    String sort;
    String period;
    Long startDatetime;
    Long endDatetime;
}
