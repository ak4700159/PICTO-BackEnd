package picto.com.photomanager.global.postDomain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.global.user.entity.User;

@Getter
@Setter
@Entity
@Table(name = "PhotoRecord", indexes = {
        @Index(name = "owner_id", columnList = "owner_id"),
        @Index(name = "photo_id", columnList = "photo_id")
})
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

    @MapsId("photoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "photo_id", nullable = false, referencedColumnName = "photo_id")
    private Photo photo;

    @Column(name = "type", nullable = false)
    private Byte type;

    @Column(name = "event_time", nullable = false)
    private Long eventTime;

}