package picto.com.generator.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "User", schema = "photo_schema")
public class User implements Persistable<Integer> {
    @Id
    @Column(name = "user_id", updatable = false)
    Integer userId;

    @Column(name = "password", nullable = false, length = 20)
    String password;

    @Column(name = "name", nullable = false, length = 20)
    String name;

    @Column(name = "email", nullable = false, length = 30)
    String email;

    @Column(name = "profile_active", nullable = false, columnDefinition = "TINYINT(1)")
    boolean profileActive;

    @Column(name = "profile_photo_path", nullable = true, length = 50)
    String profilePhotoPath;

    @Column(name = "intro", nullable = false, length = 30)
    String intro;

    @Column(name = "account_name", nullable = false, length = 20)
    String accountName;

    @Builder
    public User(int userId, String password, String name, String email, boolean profileActive, String profilePhotoPath, String intro, String accountName) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.profileActive = profileActive;
        this.profilePhotoPath = profilePhotoPath;
        this.intro = intro;
        this.password = password;
        this.accountName = accountName;
    }


    // insert 전 select 문 발생한느 것을 방지 한다. 즉 새로운 객체 엔티티임을 보장 / 실무에선 사용 X
    // 적용이 안되는 것으로 보임
    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    @Override
    public Integer getId() {
        return this.userId;
    }
}