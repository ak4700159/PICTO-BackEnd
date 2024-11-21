package picto.com.sessionscheduler.domain.session.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class FolderId {
    @Column(name = "folder_id", nullable = false)
    private Long folderId;

    @Column(name = "generator_id", nullable = false)
    private Long generatorId;

    public FolderId(Long folderId, Long generatorId) {
        this.folderId = folderId;
        this.generatorId = generatorId;
    }
}
