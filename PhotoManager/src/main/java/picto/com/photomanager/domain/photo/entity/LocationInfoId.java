package picto.com.photomanager.domain.photo.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class LocationInfoId {
    private PhotoId photoId;

    public LocationInfoId(PhotoId photoId) {
        this.photoId = photoId;
    }
}
