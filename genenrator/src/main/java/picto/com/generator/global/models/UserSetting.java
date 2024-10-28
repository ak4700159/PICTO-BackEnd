package picto.com.generator.global.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.generator.domain.user.domain.User;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "UserSetting", schema = "photo_schema")
public class UserSetting {
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
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

}