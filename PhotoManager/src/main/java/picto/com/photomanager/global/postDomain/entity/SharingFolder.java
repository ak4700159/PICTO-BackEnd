package picto.com.photomanager.global.postDomain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SharingFolder {
    @EmbeddedId
    private SharingFolderId id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "generated_time", nullable = false)
    private Long generatedTime;

    @Column(name = "content", nullable = false, length = 50)
    private String content;

}