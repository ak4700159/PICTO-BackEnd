package picto.com.usermanager.global.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.JwtException;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Component
@NoArgsConstructor
@Setter
public class JwtUtilImpl implements picto.com.usermanager.global.utils.JwtUtil {
    @Value("${jwt.secret}")
    private String TEST_SIGN_KEY;
    @Value("${jwt.refresh_secret}")
    private String REFRESH_SECRET_KEY;

    // refresh token 유효 시간 : 일주일
    final private Date REFRESH_EXPIRATION_TIME = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);
    // access token 유효 시간 : 12시간
    final private Date ACCESS_EXPIRATION_TIME = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12);
    final private String ISSUER = "picto.com";
    private Long userId;
    private boolean isAccess;

    @Override
    public String createToken() {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim("rand", rand.nextLong())
                .withClaim("userId", userId)
                .withExpiresAt(isAccess ? ACCESS_EXPIRATION_TIME : REFRESH_EXPIRATION_TIME)
                .sign(Algorithm.HMAC256(isAccess ? TEST_SIGN_KEY : REFRESH_SECRET_KEY));
    }

    @Override
    public void verifyToken(String givenToken) throws JWTVerificationException {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(isAccess ? TEST_SIGN_KEY : REFRESH_SECRET_KEY))
                    .withIssuer(ISSUER)
                    .build();
            // base64 디코딩 -> json 파싱 -> userId 추출
            DecodedJWT jwt = verifier.verify(givenToken);
            byte[] decodedPayload = Base64.getDecoder().decode(jwt.getPayload());
            JSONObject jsonObject = new JSONObject(new String(decodedPayload));
            //System.out.println("[INFO]userId from decodedJWT:" + jsonObject.get("userId"));
            //System.out.println("[INFO]userId from setting:" + userId.toString());

            if(!Objects.equals(Long.parseLong(jsonObject.get("userId").toString()), userId)) {
                throw new JWTVerificationException("[ERROR]user id match failed");
            }
        } catch (JWTVerificationException | JSONException e) {
            throw new JwtException(e.getMessage());
        }
    }
}
