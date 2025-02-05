package picto.com.generator.global.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtilImpl implements JwtUtil {
    @Value("${jwt.secret}")
    private String TEST_SIGN_KEY;
    @Value("${jwt.refresh_secret}")
    private String REFRESH_SECRET_KEY;
    private final Date REFRESH_EXPIRATION_TIME = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);
    private final Date ACCESS_EXPIRATION_TIME = new Date(System.currentTimeMillis() + 1000 * 60 * 15);
    private final String ISSUER = "picto.com";


    @Setter
    private Long userId;
    @Setter
    private boolean isAccess;


    public JwtUtilImpl() {
        super();
    }

    @Override
    public String createToken() {
        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim("userId", userId)
                .withExpiresAt(isAccess ? ACCESS_EXPIRATION_TIME : REFRESH_EXPIRATION_TIME)
                .sign(Algorithm.HMAC256(isAccess ? TEST_SIGN_KEY : REFRESH_SECRET_KEY));
    }

    @Override
    public void verifyToken(String givenToken) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(isAccess ? TEST_SIGN_KEY : REFRESH_SECRET_KEY))
                .withIssuer(ISSUER)
                .build();
        verifier.verify(givenToken);
    }
}
