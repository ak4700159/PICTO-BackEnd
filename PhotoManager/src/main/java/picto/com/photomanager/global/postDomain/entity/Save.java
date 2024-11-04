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
        @Index(name = "sharing_folder_id", columnList = "sharing_folder_id, link")
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

    @MapsId("folderId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "link", referencedColumnName = "link", nullable = false),
            @JoinColumn(name = "sharing_folder_id", referencedColumnName = "sharing_folder_id", nullable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SharingFolder sharingFolder;

    @Column(name = "saved_time", nullable = false)
    private Long savedTime;

}