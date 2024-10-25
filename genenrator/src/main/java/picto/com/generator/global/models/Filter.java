package picto.com.generator.global.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.generator.domain.user.domain.User;

@Getter
@Setter
@Entity
@Table(name = "Filter", schema = "photo_schema")
public class Filter {
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ColumnDefault("'좋아요순'")
    @Column(name = "sort", nullable = false, length = 10)
    private String sort;

    @ColumnDefault("'한 달'")
    @Column(name = "period", nullable = false, length = 10)
    private String period;

    @Column(name = "start_date")
    private Integer startDate;

}