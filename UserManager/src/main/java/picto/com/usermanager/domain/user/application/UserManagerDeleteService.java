package picto.com.usermanager.domain.user.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picto.com.usermanager.domain.user.dao.UserRepository;
import picto.com.usermanager.domain.user.dto.request.USerRequest;

@Service
@RequiredArgsConstructor
public class UserManagerDeleteService {
    private final UserRepository userRepository;

    @Transactional
    public void deleteUSer(USerRequest request){
        try {
            userRepository.delete(userRepository.getReferenceById(request.getUserId()));
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("회원 탈퇴 실패");
        }
    }
}
