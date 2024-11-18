package picto.com.photomanager.global.postDomain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.photomanager.domain.photo.entity.Photo;

@Getter
@Setter
@Entity
@Table(name = "Save", indexes = {
        @Index(name = "photo_id", columnList = "photo_id, user_id"),
        @Index(name = "generator_id", columnList = "generator_id")
})
public class Save {
    @EmbeddedId
    private SaveId id;

    @MapsId("photoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "photo_id", referencedColumnName = "photo_id", nullable = false),
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Photo photo;

    @MapsId("generatorId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "generator_id", referencedColumnName = "generator_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Folder folder;

    @Column(name = "saved_datetime", nullable = false)
    private Long savedDatetime;
}