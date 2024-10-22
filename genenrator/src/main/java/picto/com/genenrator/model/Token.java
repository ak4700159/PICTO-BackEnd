package picto.com.genenrator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;
import picto.com.genenrator.domain.user.domain.User;

import java.util.Map;

@Getter
@Setter
@Entity
public class Token {
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "refresh_token", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> refreshToken;

    @Column(name = "access_token", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> accessToken;

}