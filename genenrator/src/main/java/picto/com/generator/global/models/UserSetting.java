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

    @ColumnDefault("0")
    @Column(name = "light_mode", nullable = false)
    private Byte lightMode;

    @ColumnDefault("0")
    @Column(name = "auto_rotation", nullable = false)
    private Byte autoRotation;

    @ColumnDefault("0")
    @Column(name = "arround_alert", nullable = false)
    private Byte arroundAlert;

    @ColumnDefault("0")
    @Column(name = "popluar_alert", nullable = false)
    private Byte popluarAlert;

}