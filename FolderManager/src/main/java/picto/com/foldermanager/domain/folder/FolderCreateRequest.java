package picto.com.foldermanager.domain.folder;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FolderCreateRequest {
    private Long generatorId;
    private String name;
    private String content;
}