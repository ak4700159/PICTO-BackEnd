package picto.com.usermanager.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Session", schema = "photo_schema")
public class Session {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
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

    static public Session toEntity(User newUser, double lat, double lng) {
        return Session.builder().
                user(newUser).
                currentLat(lat).
                currentLng(lng).
                location("대구광역시 달성군 옥포읍").
                active(false).
                build();
    }
}
