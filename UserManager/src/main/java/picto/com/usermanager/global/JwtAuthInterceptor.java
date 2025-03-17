package picto.com.usermanager.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import picto.com.usermanager.global.utils.JwtUtilImpl;

import java.io.PrintWriter;

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

                    // refresh 토큰 증명이 된다면 새로운 access 토큰 발급
                    jwtUtil.setAccess(true);
                    String newAccessToken = jwtUtil.createToken();
                    throw new Exception("[INFO]" + newAccessToken);
                }
                System.out.println("[INFO]token validated");
            } catch(Exception e) {
                System.out.println("[WARN]JWT verification failed");
                response.setContentType("text/html; charset=utf-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter out = response.getWriter();
                String element = e.getMessage();
                out.print(element);
                out.flush();
                out.close();
                return false;
            }
        } catch (NullPointerException e) {
            System.out.println("[WARN]JWT verification failed");
            response.setContentType("text/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            PrintWriter out = response.getWriter();
            String element = e.getMessage();
            out.print(element);
            out.flush();
            out.close();
            return false;
        }
        return true;
    }

}
