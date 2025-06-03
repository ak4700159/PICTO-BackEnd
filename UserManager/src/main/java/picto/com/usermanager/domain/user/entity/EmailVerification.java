package picto.com.usermanager.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "EmailVerification", schema = "picto_schema")
public class EmailVerification implements Persistable<Long> {
    @Id
    @Column(name = "verification_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long verificationId;

    @Column(name = "email", nullable = false, length = 30)
    String email;

    @Column(name = "code", nullable = false, length = 6)
    String code;

    @Column(name = "request_datetime", nullable = false)
    Long requestDatetime;

    @Builder
    public EmailVerification(Long verificationId, String email, String code, Long requestDatetime) {
        this.verificationId = verificationId;
        this.email = email;
        this.code = code;
        this.requestDatetime = requestDatetime;
    }

    static public EmailVerification toMakeEntity(String email, String code, Long requestDatetime) {
        return EmailVerification.builder().email(email).code(code).requestDatetime(requestDatetime).build();
    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public Long getId() {
        return this.verificationId;
    }
}
