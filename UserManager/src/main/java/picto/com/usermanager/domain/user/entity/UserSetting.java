package picto.com.usermanager.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "UserSetting", schema = "picto_schema")
public class UserSetting {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    @Column(name = "light_mode", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean lightMode;

    @Column(name = "auto_rotation", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean autoRotation;

    @Column(name = "around_alert", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean aroundAlert;

    @Column(name = "popular_alert", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean popularAlert;

    @Builder
    public UserSetting(User user, boolean lightMode, boolean autoRotation, boolean aroundAlert, boolean popularAlert) {
        this.user = user;
        this.lightMode = lightMode;
        this.autoRotation = autoRotation;
        this.aroundAlert = aroundAlert;
        this.popularAlert = popularAlert;
    }

    static public UserSetting toEntity(User newUser){
        return UserSetting.
                builder().
                user(newUser).
                lightMode(false).
                autoRotation(false).
                aroundAlert(true).
                popularAlert(true).
                build();
    }

}