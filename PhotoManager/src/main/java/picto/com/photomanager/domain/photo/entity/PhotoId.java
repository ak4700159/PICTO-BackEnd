package picto.com.photomanager.domain.photo.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class PhotoId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "photo_id")
    private int photoID;

    @Column(name = "user_id", nullable = false)
    private int userId;

    public PhotoId(int photoID, int userId) {
        this.photoID = photoID;
        this.userId = userId;
    }

}
