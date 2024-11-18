package picto.com.photomanager.domain.photo.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class LocationInfo implements Serializable {
    @EmbeddedId
    private LocationInfoId id;

    @MapsId("photoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumns({
            @JoinColumn(name = "photo_id", nullable = false, referencedColumnName = "photo_id"),
            @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    })
    Photo photo;

    @Column(name = "large_name", length = 30)
    String largeName;

    @Column(name = "middle_name", length = 30)
    String middleName;

    @Column(name = "small_name", length = 30)
    String smallName;

    @Builder
    public LocationInfo(LocationInfoId id, Photo photo, String largeName, String middleName, String smallName) {
        this.id = id;
        this.photo = photo;
        this.largeName = largeName;
        this.middleName = middleName;
        this.smallName = smallName;
    }
}
