package picto.com.generator.global.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.generator.domain.photo.domain.Photo;

@Getter
@Setter
@Entity
@Table(name = "TitleList", schema = "photo_schema", indexes = {
        @Index(name = "name", columnList = "name"),
        @Index(name = "user_id", columnList = "user_id")
})
public class TitleList {
    @Id
    @Column(name = "title_list_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "name", nullable = false)
    private Title name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private Photo user;

}