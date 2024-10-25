package picto.com.generator.global.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.generator.domain.photo.domain.Photo;
import picto.com.generator.domain.user.domain.User;

@Getter
@Setter
@Entity
@Table(name = "PhotoRecord", schema = "photo_schema")
public class PhotoRecord {
    @EmbeddedId
    private PhotoRecordId id;

    @MapsId("agentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent;

    @MapsId("ownerId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @MapsId("photoPath")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "photo_path", nullable = false, referencedColumnName = "photo_path")
    private Photo photoPath;

    @Column(name = "type", nullable = false)
    private Byte type;

    @Column(name = "event_time", nullable = false)
    private Integer eventTime;

}