package picto.com.genenrator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.genenrator.domain.user.domain.User;

@Getter
@Setter
@Entity
public class Filter {
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 좋아요순 근처거리순, 조회수순, 최근업로드순
    @ColumnDefault("좋아요순")
    @Column(name = "sort", length = 10)
    private String sort;

    // 하루, 1주일,한달, 일년, 모든날짜
    @ColumnDefault("모든날짜")
    @Column(name = "period", length = 10)
    private String period;
}