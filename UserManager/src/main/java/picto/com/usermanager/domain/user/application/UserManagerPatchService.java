package picto.com.usermanager.domain.user.application;

import jakarta.transaction.Transactional;
import jdk.jfr.Timespan;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import picto.com.usermanager.domain.user.dao.UserRepository;
import picto.com.usermanager.domain.user.dto.request.USerRequest;
import picto.com.usermanager.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class UserManagerPatchService {
    final UserRepository userRepository;

    @Transactional
    public void fetchUser(USerRequest request) throws IllegalAccessException {
        User findUser = userRepository.getReferenceById(request.getUserId());
        if (findUser != null) {
            // 비밀번호 변경인 경우
            if(request.getType().equals("password")){
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String newPasswrod = passwordEncoder.encode(request.getPassword());
                findUser.setPassword(newPasswrod);
            }
            // 사용자 정보 변경
            else if(request.getType().equals("info")){
                findUser.setAccountName(request.getAccountName());
                findUser.setEmail(request.getEmail());
                findUser.setName(request.getName());
                findUser.setIntro(request.getIntro());
                findUser.setProfileActive(request.getProfileActive());
                findUser.setProfilePhotoPath(request.getProfilePhotoPath());
            }
            userRepository.save(findUser);
        }
        else {
            throw new IllegalAccessException("NOT FOUND USER");
        }
    }

    public void fetchUserSetting(){

    }

    public void fetchMark(){

    }

    public void fetchBlock(){

    }

    public void fetchTagList(){

    }

}
