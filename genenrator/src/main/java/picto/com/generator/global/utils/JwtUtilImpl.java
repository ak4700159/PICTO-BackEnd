package picto.com.generator.global.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

public class JwtUtilImpl implements JwtUtil {
    private final String TEST_SIGN_KEY = "TESTKEY";
    // 유효 토큰 일주일
    private final Date EXPITRED_TIME = new Date(System.currentTimeMillis() + 604800000	);
    private final String ISSUER = "picto.com";
    private final Long userId;

    public JwtUtilImpl(Long userId) {
        super();
        this.userId = userId;
    }

    @Override
    public String createToken() {
        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim("userId", userId)
                .withExpiresAt(EXPITRED_TIME)
                .sign(Algorithm.HMAC256(TEST_SIGN_KEY));
    }

    @Override
    public void verifyToken(String givenToken) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TEST_SIGN_KEY))
                .withIssuer(ISSUER)
                .build();
        verifier.verify(givenToken);
    }
}
