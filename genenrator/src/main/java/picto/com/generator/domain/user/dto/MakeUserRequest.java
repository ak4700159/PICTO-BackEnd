package picto.com.generator.domain.user.dto;

import lombok.*;
import picto.com.generator.domain.user.domain.User;

import java.util.Random;



@Getter
@NoArgsConstructor // 일부로 접근 권한을 public으로 허용한다. 왜냐하면 외부에서 생성되는 dto이기 때문이다.
public class MakeUserRequest {
    private int user_id;
    private String password;
    private String name;
    private String email;
    private int profile_active;
    private String intro;
    private String account_name;

    // count = user_id
    // 1부터 차례대로 user_id가 쌓이게 된다.
    public User toRandomEntity(int count) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return User.builder().
                name(Integer.toString(count)).
                account_name(Integer.toString(count)).
                intro("안녕하세요 저는 " + count + " 입니다.").
                password(Integer.toString(count)).
                email(generatedString + "@gmail.com").
                profile_active(true).
                user_id(count).
                profile_photo_path(null).
                build();
    }
}
