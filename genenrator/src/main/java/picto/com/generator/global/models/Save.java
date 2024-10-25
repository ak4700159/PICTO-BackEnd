package picto.com.generator.global.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.generator.domain.photo.domain.Photo;

@Getter
@Setter
@Entity
@Table(name = "Save", schema = "photo_schema")
public class Save {
    @EmbeddedId
    private SaveId id;

    @MapsId("id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "photo_path", referencedColumnName = "photo_path", nullable = false),
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Photo photo;

    @MapsId("sharingFolderId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sharing_folder_id", nullable = false, referencedColumnName = "sharing_folder_id")
    private SharingFolder sharingFolder;

    @Column(name = "saved_time", nullable = false)
    private Integer savedTime;

}