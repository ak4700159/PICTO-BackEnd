package picto.com.genenrator.domain.photo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class PhotoId implements Serializable {
    @Column(name = "photo_path")
    private String photoPath;

    @Column(name = "tag")
    private String tag;
}
