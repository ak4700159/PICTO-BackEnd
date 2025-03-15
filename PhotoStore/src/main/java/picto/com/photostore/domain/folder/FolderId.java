package picto.com.photostore.domain.folder;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
public class FolderId implements Serializable {
    @Column(name = "folder_id")
    private Long folderId;

    @Column(name = "generator_id")
    private Long generatorId;

    public FolderId(Long generatorId, Long folderId) {
        this.folderId = folderId;
        this.generatorId = generatorId;
    }
}