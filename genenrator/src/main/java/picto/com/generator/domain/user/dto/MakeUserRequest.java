package picto.com.generator.domain.user.dto;

import lombok.*;
import picto.com.generator.domain.user.entity.User;

import java.util.Random;



@Getter
@NoArgsConstructor // 일부로 접근 권한을 public으로 허용한다. 왜냐하면 외부에서 생성되는 dto이기 때문이다.
public class MakeUserRequest {
    private String password;
    private String name;
    private String email;
    private int profile_active;
    private String intro;
    private String account_name;

    // count = user_id
    // 1부터 차례대로 user_id가 쌓이게 된다.
    public User toRandomEntity(Long count) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return User.builder().
                name(generatedString).
                accountName(generatedString).
                intro("안녕하세요 저는 " + generatedString + " 입니다.").
                password(Long.toString(count)).
                email(generatedString + "@gmail.com").
                profileActive(true).
                profilePhotoPath(null).
                build();
    }
}
