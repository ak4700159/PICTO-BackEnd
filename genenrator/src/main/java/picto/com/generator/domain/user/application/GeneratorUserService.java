package picto.com.generator.domain.user.application;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import picto.com.generator.domain.user.dao.UserRepository;
import picto.com.generator.domain.user.domain.User;
import picto.com.generator.domain.user.dto.MakeUserRequest;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneratorUserService {
    // 무조건 final 로 설정해야 롬북이 작동함.
    private final UserRepository userRepository;

    public User addUser(){
        User newUser = new MakeUserRequest().toRandomEntity(1);
        userRepository.save(newUser);
        System.out.println("생성 : " + newUser);
        return newUser;
    }

    public List<User> makeUser100(){
        List<User> users = new ArrayList<User>();
        for(int i = 2; i <= 100; i++){
            User newUser = new MakeUserRequest().toRandomEntity(i);
            userRepository.save(newUser);
            users.add(newUser);
        }
        return users;
    }

    public List<User> findAllUser(){
        return userRepository.findAll();
    }
}
