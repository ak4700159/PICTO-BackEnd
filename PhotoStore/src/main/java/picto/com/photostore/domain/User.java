package picto.com.photostore.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "User", schema = "photo_schema")
public class User implements Persistable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "profile_active", nullable = false, columnDefinition = "tinyint")
    private boolean profileActive;

    @Column(name = "profile_photo_path", length = 50)
    private String profilePhotoPath;

    @Column(name = "intro", nullable = false, length = 30)
    private String intro;

    @Column(name = "account_name", nullable = false, length = 20)
    private String accountName;

    @Builder
    public User(Long userId, String password, String name, String email,
                boolean profileActive, String profilePhotoPath,
                String intro, String accountName) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.profileActive = profileActive;
        this.profilePhotoPath = profilePhotoPath;
        this.intro = intro;
        this.accountName = accountName;
    }

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public boolean isNew() {
        return userId == null;
    }
}