package picto.com.generator.global.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.generator.domain.user.entity.User;
import picto.com.generator.global.entity.Token;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class AddDefaultToken {

    public Token toEntity(User newUser) {
        Map<String, Object> accessToken = new HashMap<>();
        Map<String, Object> refreshToken = new HashMap<>();

        return Token.
                builder().
                accessToken(accessToken).
                refreshToken(refreshToken).
                user(newUser).
                build();
    }

}
