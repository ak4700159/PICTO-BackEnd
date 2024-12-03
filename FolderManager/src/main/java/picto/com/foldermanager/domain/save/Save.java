package picto.com.foldermanager.domain.save;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.foldermanager.domain.folder.Folder;
import picto.com.foldermanager.domain.photo.Photo;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Save")
public class Save {
    @EmbeddedId
    private SaveId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private Photo photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", insertable = false, updatable = false)
    private Folder folder;

    @Column(name = "saved_datetime")
    private Long savedDatetime;

    @Builder
    public Save(SaveId id, Photo photo, Folder folder) {
        this.id = id;
        this.photo = photo;
        this.folder = folder;
        this.savedDatetime = System.currentTimeMillis();
    }
}