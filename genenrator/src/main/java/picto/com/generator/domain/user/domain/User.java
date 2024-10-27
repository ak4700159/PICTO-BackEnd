package picto.com.generator.domain.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "User", schema = "photo_schema")
public class User {
    @Id
    @Column(name = "user_id", updatable = false)
    int user_id;

    @Column(name = "password", nullable = false, length = 20)
    String password;

    @Column(name = "name", nullable = false, length = 20)
    String name;

    @Column(name = "email", nullable = false, length = 30)
    String email;

    @Column(name = "profile_active", nullable = false, columnDefinition = "TINYINT(1)")
    boolean profile_active;

    @Column(name = "profile_photo_path", nullable = true, length = 50)
    String profile_photo_path;

    @Column(name = "intro", nullable = false, length = 30)
    String intro;

    @Column(name = "account_name", nullable = false, length = 20)
    String account_name;

    @Builder
    public User(int user_id,String password, String name, String email, boolean profile_active, String profile_photo_path, String intro, String account_name) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.profile_active = profile_active;
        this.profile_photo_path = profile_photo_path;
        this.intro = intro;
        this.password = password;
        this.account_name = account_name;
    }
}
