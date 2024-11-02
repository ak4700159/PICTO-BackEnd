package picto.com.generator.global.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class PhotoRecordId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1878673855752472658L;
    @Column(name = "agent_id", nullable = false)
    private Integer agentId;

    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;


    @Column(name = "photo_id", nullable = false, length = 50)
    private int photoId;

    public PhotoRecordId(Integer agentId, Integer ownerId, Integer photoId) {
        this.agentId = agentId;
        this.ownerId = ownerId;
        this.photoId = photoId;
    }

}