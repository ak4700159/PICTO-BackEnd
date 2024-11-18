package picto.com.usermanager.domain.user.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class PhotoId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "photo_id")
    private Integer photoId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    public PhotoId(int photoId, int userId) {
        this.photoId = photoId;
        this.userId = userId;
    }

}
