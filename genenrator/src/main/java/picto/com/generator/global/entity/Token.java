package picto.com.generator.global.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;
import picto.com.generator.domain.user.entity.User;

import java.util.Map;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Token", schema = "photo_schema")
public class Token {
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    @Column(name = "refresh_token", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> refreshToken;

    @Column(name = "access_token", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> accessToken;

    @Builder
    public Token(User user, Map<String, Object> refreshToken, Map<String, Object> accessToken) {
        this.accessToken =  accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }
}