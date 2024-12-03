package picto.com.photostore.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocationInfoResponse {
    private String largeName;
    private String middleName;
    private String smallName;
}