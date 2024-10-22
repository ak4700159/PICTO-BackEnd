package picto.com.genenrator.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.genenrator.domain.photo.domain.Photo;
import picto.com.genenrator.domain.user.domain.User;

@Getter
@Setter
@Entity
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

    @MapsId("tag")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "tag", nullable = false, referencedColumnName = "tag")
    private Tag tag;

    @MapsId("category")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "category", nullable = false)
    private Category category;

    @Column(name = "type", nullable = false)
    //@ColumnDefault("0")
    private Byte type;

    @Column(name = "event_time", nullable = false)
    private Integer eventTime;

}