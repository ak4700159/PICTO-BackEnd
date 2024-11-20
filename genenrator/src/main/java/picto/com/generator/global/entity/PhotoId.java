package picto.com.generator.global.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public PhotoId(Long photoId, Long userId) {
        this.photoId = photoId;
        this.userId = userId;
    }

}
