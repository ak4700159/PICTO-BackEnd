package picto.com.generator.domain.user.restapi;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import picto.com.generator.domain.user.application.GeneratorUserService;
import picto.com.generator.domain.user.domain.User;
import picto.com.generator.domain.user.dto.FindUserEmail;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GeneratorUserController {
    private final GeneratorUserService generatorUserService;

    @GetMapping("api/generator/user")
    public ResponseEntity<List<User>> findAllUser () {
        System.out.println("user found");
        List<User> users = generatorUserService.findAllUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    @GetMapping("api/generator/user/email")
    public ResponseEntity<List<User>> findEmailUser (@RequestBody FindUserEmail findUserEmail) {
        System.out.println("user found");
        List<User> users = generatorUserService.findEmailUsers(findUserEmail.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }


    // 특정 아이디로 검색
    @PostMapping("api/generator/user/100")
    public ResponseEntity<List<User>> postIdUser () {
        System.out.println("User 100 created request");
        List<User> users = generatorUserService.makeUser100();
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }
}
