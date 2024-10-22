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
public class Session {
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "current_lat")
    @ColumnDefault("0.0")
    private Double currentLat;

    @Column(name = "current_lng")
    @ColumnDefault("0.0")
    private Double currentLng;

    @Column(name = "location", length = 50)
    @ColumnDefault("대구 광역시 달성군 옥포읍")
    private String location;

    @Column(name = "active")
    @ColumnDefault("0")
    private Boolean active;

}