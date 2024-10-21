package picto.com.genenrator.domain.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, columnDefinition = "int")
    int user_id;

    @Column(name = "password", nullable = false,columnDefinition = "varchar(20)")
    String password;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(20)")
    String name;

    @Column(name = "email", nullable = false, columnDefinition = "varchar(20)")
    String email;

    @Column(name = "profile_active", nullable = false, columnDefinition = "tinyint")
    boolean profile_active;

    @Column(name = "profile_photo_path", nullable = true, columnDefinition = "varchar(30)")
    String profile_photo_path;

    @Column(name = "intro", nullable = false, columnDefinition = "varchar(20)")
    String intro;

    @Column(name = "account_name", nullable = false, columnDefinition = "varchar(10)")
    String account_name;


}
