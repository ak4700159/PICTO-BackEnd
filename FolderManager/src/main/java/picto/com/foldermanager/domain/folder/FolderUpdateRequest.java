package picto.com.foldermanager.domain.folder;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FolderUpdateRequest {
    private String name;
    private String content;
}