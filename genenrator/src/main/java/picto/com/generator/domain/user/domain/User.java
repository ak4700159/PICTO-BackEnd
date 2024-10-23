package picto.com.generator.domain.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(name = "user_id", updatable = false)
    int user_id;

    @Column(name = "password", nullable = false,columnDefinition = "varchar(20)")
    String password;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(20)")
    String name;

    @Column(name = "email", nullable = false, columnDefinition = "varchar(20)")
    String email;

    @Column(name = "profile_active", nullable = false, columnDefinition = "tinyint")
    int profile_active;

    @Column(name = "profile_photo_path", nullable = true, columnDefinition = "varchar(30)")
    String profile_photo_path;

    @Column(name = "intro", nullable = false, columnDefinition = "varchar(20)")
    String intro;

    @Column(name = "account_name", nullable = false, columnDefinition = "varchar(10)")
    String account_name;

    @Builder
    public User(int user_id,String password, String name, String email, int profile_active, String profile_photo_path, String intro, String account_name) {
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
