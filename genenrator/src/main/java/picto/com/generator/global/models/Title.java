package picto.com.generator.global.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import picto.com.generator.domain.photo.domain.Photo;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Title", schema = "photo_schema")
public class Title {
    @Id
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "content", nullable = false, length = 40)
    private String content;

    @Column(name = "`condition`", nullable = false, length = 40)
    private String condition;

    @ManyToMany
    @JoinTable(name = "TitleList",
            joinColumns = @JoinColumn(name = "name"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Photo> photos = new LinkedHashSet<>();

}