package picto.com.usermanager.global.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.JwtException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

public class JwtUtilImpl implements picto.com.usermanager.global.utils.JwtUtil {
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
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(isAccess ? TEST_SIGN_KEY : REFRESH_SECRET_KEY))
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT jwt = verifier.verify(givenToken);
            if(!Objects.equals(jwt.getClaim("userId").asLong(), userId)) {
                throw new JWTVerificationException("user id match failed");
            }
        } catch (JWTVerificationException e) {
            throw new JwtException("JWT verification failed");
        }
    }
}
