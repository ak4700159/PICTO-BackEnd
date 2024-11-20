package picto.com.usermanager.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import picto.com.usermanager.domain.user.dao.TokenRepository;
import picto.com.usermanager.domain.user.dao.UserRepository;
import picto.com.usermanager.domain.user.entity.Token;
import picto.com.usermanager.domain.user.entity.User;
import picto.com.usermanager.global.utils.JwtUtilImpl;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {
    private final JwtUtilImpl jwtUtil;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private String HEAD_TOKEN_KEY =  "Access-Token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = userRepository.findById(Long.parseLong(request.getHeader("User-Id")))
                .orElseThrow(()->new IllegalArgumentException("User Not Found"));
        Token findToken = tokenRepository.findById(user.getId())
                .orElseThrow(()->new IllegalArgumentException("Token Not Found"));
        String token = request.getHeader(HEAD_TOKEN_KEY);
        verifyToken(token, findToken.getAccessToken());
        System.out.println(user.getUserId() + "사용자에 대한 토큰 유효성 검사 통과");
        return true;
    }

    public void verifyToken(String token, String accessToken) {
        if(!token.equals(accessToken)) {
            throw new IllegalArgumentException("Invalid token");
        }
        jwtUtil.verifyToken(token);
    }

}
