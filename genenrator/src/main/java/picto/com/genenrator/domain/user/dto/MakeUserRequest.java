package picto.com.genenrator.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.genenrator.domain.user.domain.User;

import java.util.Random;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MakeUserRequest {
    private int user_id;
    private String password;
    private String name;
    private String email;
    private int profile_active;
    private String intro;
    private String account_name;

    public User toRandomEntity(int count) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        System.out.printf("user creates : %s", generatedString);

        return User.builder().
                name(generatedString).
                email(generatedString + "@gmail.com").
                profile_active(0).
                user_id(count).
                profile_photo_path(null).
                account_name(generatedString).
                intro(generatedString).
                build();
    }
}
