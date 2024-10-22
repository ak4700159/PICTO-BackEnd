package picto.com.genenrator.domain.user.restapi;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import picto.com.genenrator.domain.user.application.GeneratorUserService;
import picto.com.genenrator.domain.user.domain.User;
import picto.com.genenrator.domain.user.dto.AddUserRequest;
import picto.com.genenrator.domain.user.dto.MakeUserRequest;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GeneratorUserController {
    @Autowired
    private final GeneratorUserService generatorUserService;

    @PostMapping("api/generator/user")
    public ResponseEntity<List<User>> addUser100 (@RequestBody AddUserRequest user) {
        System.out.println("요청");
        List<User> users = generatorUserService.makeUser100();
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    @GetMapping("api/generator/user")
    public ResponseEntity<User> getUser () {
        User user = generatorUserService.addUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
