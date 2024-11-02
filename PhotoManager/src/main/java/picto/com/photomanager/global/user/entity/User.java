package picto.com.photomanager.global.user.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "User", schema = "photo_schema")
public class User implements Persistable<Integer> {
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


    // insert 전 select 문 발생한느 것을 방지 한다. 즉 새로운 객체 엔티티임을 보장 / 실무에선 사용 X
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
        return this.user_id;
    }
}
