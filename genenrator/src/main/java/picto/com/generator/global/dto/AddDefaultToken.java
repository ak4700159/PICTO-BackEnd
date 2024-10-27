package picto.com.generator.global.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.generator.domain.user.domain.User;
import picto.com.generator.global.models.Token;

@Getter
@NoArgsConstructor
public class AddDefaultToken {

    public Token toEntity(User newUser) {
        return Token.
                builder().
                accessToken(null).
                refreshToken(null).
                id(newUser.getUser_id()).
                build();
    }

}
