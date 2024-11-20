package picto.com.photomanager.global.postDomain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
public class FolderId {
    @Column(name = "folder_id")
    private Long folderId;

    @Column(name = "generator_id")
    private Long generatorId;

    public FolderId(Long generatorId, Long folderId) {
        this.folderId = folderId;
        this.generatorId = generatorId;

    }
}
