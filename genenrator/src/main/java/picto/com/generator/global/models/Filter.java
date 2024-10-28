package picto.com.generator.global.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.generator.domain.user.domain.User;

import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Filter", schema = "photo_schema")
public class Filter {
    // user_id 는 생성자에 포함 X 왜냐하면 User 객체에서 식별하기 때문
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer id;

    // FK 해당 테이블의 PK 로 사용시 이를 명시하기 위해서 사용
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

    @Column(name = "start_time")
    private Long startTime;

    // 
    @Builder
    public Filter(String sort, String period, Long startTime, User user) {
        this.user = user;
        this.sort = sort;
        this.period = period;
        this.startTime = startTime;
    }
}