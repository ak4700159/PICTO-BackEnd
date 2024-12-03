package picto.com.foldermanager.domain.share;

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
public class ShareId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "folder_id")
    private Long folderId;
}