package picto.com.photomanager.domain.photo.dto.response.GetKakaoResponse;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetKakaoLocationInfoResponse {
    private Meta meta;
    private List<Document> documents;
}

