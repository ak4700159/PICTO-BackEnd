package picto.com.usermanager.global;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import picto.com.usermanager.global.utils.JwtUtilImpl;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {
    private final JwtUtilImpl jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getHeader("Access-Token");
        String refreshToken = request.getHeader("Refresh-Token");
        String userId = request.getHeader("User-Id");
        try{
            jwtUtil.setUserId(Long.parseLong(userId));
            try {
                // access token이 아니면 refresh token 접근
                if(accessToken != null) {
                    jwtUtil.setAccess(true);
                    jwtUtil.verifyToken(accessToken);
                    System.out.println("[INFO]access token validate");
                } else {
                    jwtUtil.setAccess(false);
                    jwtUtil.verifyToken(refreshToken);
                    System.out.println("[INFO]refresh token validate");

                    // refresh 토큰 증명이 된다면
                    jwtUtil.setAccess(true);
                    String newAccessToken = jwtUtil.createToken();
                    throw new Exception("[INFO]" + newAccessToken);
                }
                System.out.println("[INFO]token validated");
            } catch(JWTVerificationException e) {
                System.out.println(e.getMessage());
                throw new Exception("[ERROR]login");
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
        return true;
    }

}
