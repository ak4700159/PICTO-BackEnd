package picto.com.generator.global.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.generator.domain.user.entity.User;
import picto.com.generator.global.entity.Token;
import picto.com.generator.global.utils.JwtUtilImpl;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class AddDefaultToken {
    JwtUtilImpl jwtUtil;

    public Token toEntity(User newUser) {
        jwtUtil= new JwtUtilImpl(newUser.getUserId());
        String accessToken = jwtUtil.createToken();
        String refreshToken = jwtUtil.createToken();

        return Token.
                builder().
                accessToken(accessToken).
                refreshToken(refreshToken).
                user(newUser).
                build();
    }

}
