package picto.com.photostore.domain.folder;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FolderUpdateRequest {
    private String name;
    private String content;
}