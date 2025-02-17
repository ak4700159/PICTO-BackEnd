package picto.com.photomanager.global.getDomain.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.photomanager.domain.user.entity.User;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Filter", schema = "picto_schema")
public class Filter {
    // user_id 는 생성자에 포함 X 왜냐하면 User 객체에서 식별하기 때문
    @Id
    @Column(name = "user_id")
    private Long userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "sort", length = 10)
    private String sort;

    @Column(name = "period", length = 10)
    private String period;

    @Builder
    public Filter(String sort, String period ,User user) {
        this.user = user;
        this.sort = sort;
        this.period = period;
    }
}