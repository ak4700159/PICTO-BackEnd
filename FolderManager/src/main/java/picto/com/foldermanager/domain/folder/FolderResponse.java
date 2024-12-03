package picto.com.foldermanager.domain.folder;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FolderResponse {
    private Long id;
    private Long generatorId;
    private String name;
    private String content;
    private Long createdDatetime;
    private String link;

    public static picto.com.foldermanager.domain.folder.FolderResponse from(Folder folder) {
        return picto.com.foldermanager.domain.folder.FolderResponse.builder()
                .id(folder.getId())
                .generatorId(folder.getGenerator().getId())
                .name(folder.getName())
                .content(folder.getContent())
                .createdDatetime(folder.getCreatedDatetime())
                .link(folder.getLink())
                .build();
    }
}