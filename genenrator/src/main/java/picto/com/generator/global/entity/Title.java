package picto.com.generator.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Title", schema = "picto_schema")
public class Title {
    @Id @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "content", nullable = false, length = 40)
    private String content;

    @Column(name = "condition", nullable = false, length = 40)
    private String condition;

}