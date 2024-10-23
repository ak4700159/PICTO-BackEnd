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
import picto.com.generator.domain.user.dto.AddUserRequest;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GeneratorUserController {
    private final GeneratorUserService generatorUserService;


    @GetMapping("api/generator/user/100")
    public ResponseEntity<List<User>> addUser100 () {
        System.out.println("User 100 생성 요청");
        List<User> users = generatorUserService.makeUser100();
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }


    @GetMapping("api/generator/user")
    public ResponseEntity<User> getUser () {
        System.out.println("Get 요청");
        User user = generatorUserService.addUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
