package picto.com.usermanager.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.usermanager.domain.user.entity.Folder;
import picto.com.usermanager.domain.user.entity.Photo;
import picto.com.usermanager.domain.user.entity.SaveId;

@Getter
@Setter
@Entity
@Table(name = "Save", indexes = {
        @Index(name = "photo_id", columnList = "photo_id, user_id"),
        @Index(name = "folder_id", columnList = "folder_id, generator_id")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Save {
    @EmbeddedId
    private SaveId id;

    @MapsId("photoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "photo_id", referencedColumnName = "photo_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Photo photo;

    @MapsId("folderId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "folder_id", referencedColumnName = "folder_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Folder folder;

    @Column(name = "saved_datetime", nullable = false)
    private Long savedDatetime;

    @Builder
    public Save(SaveId id, Photo photo, Folder folder, Long savedDatetime) {
        this.id = id;
        this.photo = photo;
        this.folder = folder;
        this.savedDatetime = savedDatetime;
    }
}