package picto.com.genenrator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Title {
    @Id
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "content", nullable = false, length = 40)
    private String content;

    @Column(name = "`condition`", nullable = false, length = 40)
    private String condition;

}