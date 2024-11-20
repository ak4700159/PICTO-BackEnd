package picto.com.photomanager.global.getDomain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.photomanager.domain.user.entity.User;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Session", schema = "photo_schema")
public class Session {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "current_lat")
    private Double currentLat;

    @Column(name = "current_lng")
    private Double currentLng;

    @Column(name = "location", length = 50)
    private String location;

    @Column(name = "active")
    private Boolean active;

    @Builder
    public Session(User user, Double currentLat, Double currentLng, String location, Boolean active) {
        this.user = user;
        this.currentLat = currentLat;
        this.currentLng = currentLng;
        this.location = location;
        this.active = active;
    }

}