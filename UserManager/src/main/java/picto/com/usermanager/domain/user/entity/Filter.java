package picto.com.usermanager.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;


@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Filter", schema = "photo_schema")
public class Filter {
    // user_id 는 생성자에 포함 X 왜냐하면 User 객체에서 식별하기 때문
    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // FK 해당 테이블의 PK 로 사용시 이를 명시하기 위해서 사용
    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    @ColumnDefault("좋아요순")
    @Column(name = "sort", nullable = false, length = 10)
    private String sort;

    @ColumnDefault("한달")
    @Column(name = "period", nullable = false, length = 10)
    private String period;

    @Column(name = "start_datetime")
    private Long startDateTime;

    @Column(name = "end_datetime", nullable = true)
    private Long endDateTime;

    @Builder
    public Filter(String sort, String period, Long startDateTime, Long endDateTime ,User user) {
        this.user = user;
        this.sort = sort;
        this.period = period;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    // 처음 생성시 "좋아요순" "한 달"
    static public Filter toEntity(User newUser){
        return Filter.builder().
                user(newUser).
                sort("좋아요순").
                period("한달").
                startDateTime(new Date().getTime()).
                endDateTime(new Date().getTime()).
                build();
    }
}