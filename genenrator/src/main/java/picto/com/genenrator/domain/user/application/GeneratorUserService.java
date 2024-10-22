package picto.com.genenrator.domain.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import picto.com.genenrator.domain.user.dao.UserRepository;
import picto.com.genenrator.domain.user.domain.User;
import picto.com.genenrator.domain.user.dto.MakeUserRequest;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GeneratorUserService {
    @Autowired
    private final UserRepository userRepository;

    public User addUser(){
        User newUser = new MakeUserRequest().toRandomEntity(2);
        userRepository.save(newUser);
        System.out.println("생성 : " + newUser);
        return newUser;
    }

    public List<User> makeUser100(){
        List<User> users = new ArrayList<User>();
        for(int i = 0; i < 100; i++){
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
