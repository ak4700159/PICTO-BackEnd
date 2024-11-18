package picto.com.usermanager.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import picto.com.usermanager.domain.user.dao.SessionRepository;
import picto.com.usermanager.domain.user.dao.TokenRepository;
import picto.com.usermanager.domain.user.dao.UserRepository;
import picto.com.usermanager.domain.user.entity.Session;
import picto.com.usermanager.domain.user.entity.Token;
import picto.com.usermanager.domain.user.entity.User;
import picto.com.usermanager.global.utils.JwtUtil;

public class JwtAuthInterceptor implements HandlerInterceptor {
    private JwtUtil jwtUtil;
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private String HEAD_TOKEN_KEY =  "token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = userRepository.findById(Integer.parseInt(request.getHeader("userId")))
                .orElseThrow(()->new IllegalArgumentException("User Not Found"));
        Token findToken = tokenRepository.findById(user.getId())
                .orElseThrow(()->new IllegalArgumentException("Token Not Found"));
        String token = request.getHeader(HEAD_TOKEN_KEY);
        verifyToken(token, findToken.getAccessToken());
        return true;
    }

    public void verifyToken(String token, String accessToken) {
        if(! token.equals(accessToken)) {
            throw new IllegalArgumentException("Invalid token");
        }
        jwtUtil.verifyToken(token);
    }

}
