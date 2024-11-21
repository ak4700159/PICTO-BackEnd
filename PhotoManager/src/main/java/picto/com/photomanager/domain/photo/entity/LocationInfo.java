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
    @Id
    @Column(name = "photo_id", nullable = false)
    private Long photoId;

    @MapsId("photoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "photo_id", nullable = false, referencedColumnName = "photo_id")
    Photo photo;

    @Column(name = "large_name", length = 30)
    String largeName;

    @Column(name = "middle_name", length = 30)
    String middleName;

    @Column(name = "small_name", length = 30)
    String smallName;

    @Builder
    public LocationInfo(Long photoId, Photo photo, String largeName, String middleName, String smallName) {
        this.photoId = photoId;
        this.photo = photo;
        this.largeName = largeName;
        this.middleName = middleName;
        this.smallName = smallName;
    }
}
