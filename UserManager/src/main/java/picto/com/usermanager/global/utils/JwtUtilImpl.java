package picto.com.usermanager.global.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@NoArgsConstructor
@Setter
public class JwtUtilImpl implements picto.com.usermanager.global.utils.JwtUtil {
    @Value("${jwt.secret}")
    private String TEST_SIGN_KEY;
    @Value("${jwt.refresh_secret}")
    private String REFRESH_SECRET_KEY;

    final private String ISSUER = "picto.com";
    private Long userId;
    private boolean isAccess;

    @Override
    public String createToken() {
        // refresh token 유효 시간 : 일주일
        Date REFRESH_EXPIRATION_TIME = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);
        // access token 유효 시간 : 12시간
        Date ACCESS_EXPIRATION_TIME = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12);

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
    public void verifyToken(String givenToken) throws Exception {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(isAccess ? TEST_SIGN_KEY : REFRESH_SECRET_KEY))
                    .withIssuer(ISSUER)
                    .build();
            // base64 디코딩 -> json 파싱 -> userId 추출
            DecodedJWT jwt = verifier.verify(givenToken);
            byte[] decodedPayload = Base64.getDecoder().decode(jwt.getPayload());
            String convert = new String(decodedPayload, StandardCharsets.UTF_8);
            System.out.println("[INFO]decoded JWT : " + convert);
            JSONObject jsonObject = new JSONObject(new String(decodedPayload));
            //System.out.println("[INFO]userId from decodedJWT:" + jsonObject.get("userId"));
            //System.out.println("[INFO]userId from setting:" + userId.toString());

            if (!Objects.equals(Long.parseLong(jsonObject.get("userId").toString()), userId)) {
                throw new JWTVerificationException("[ERROR]user id match failed");
            }
            // 엑세스 토큰인 경우 리프레쉬 토큰이 만료되지 않은 경우 엑세스 토큰을 만들어 발행^M
            if (!isAccess) {
                isAccess = true;
                String newAccessToken = createToken();
                throw new Exception("[TOKEN]" + newAccessToken);
            }
        } catch (Exception e) {
            if(!e.getMessage().contains("[Token]")) {
                throw new Exception("[ERROR]token error : " + e.getMessage());
            }
            throw new Exception(e.getMessage());
        }
    }
}
