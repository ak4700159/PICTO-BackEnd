package picto.com.foldermanager.domain.save;

import jakarta.persistence.*;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SaveId implements Serializable {
    @Column(name = "photo_id")
    private Long photoId;

    @Column(name = "folder_id")
    private Long folderId;
}