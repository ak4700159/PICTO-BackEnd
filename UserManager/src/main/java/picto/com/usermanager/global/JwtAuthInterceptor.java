package picto.com.usermanager.global;

import com.auth0.jwt.exceptions.JWTVerificationException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import picto.com.usermanager.global.utils.JwtUtilImpl;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        JwtUtilImpl jwtUtil = new JwtUtilImpl();
        try{
            String accessToken = request.getHeader("Access-Token");
            String refreshToken = request.getHeader("Refresh-Token");
            String userId = request.getHeader("user-Id");

            jwtUtil.setUserId(Long.parseLong(userId));
            try {
                // access token이 아니면 refresh token 접근
                if(accessToken != null) {
                    jwtUtil.setAccess(true);
                    jwtUtil.verifyToken(accessToken);
                } else {
                    jwtUtil.setAccess(false);
                    jwtUtil.verifyToken(refreshToken);
                }
            } catch(JWTVerificationException | JwtException e) {
                System.out.println(e.getMessage());
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

}
