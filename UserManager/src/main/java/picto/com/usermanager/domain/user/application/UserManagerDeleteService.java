package picto.com.usermanager.domain.user.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picto.com.usermanager.domain.user.dao.BlockRepository;
import picto.com.usermanager.domain.user.dao.MarkRepository;
import picto.com.usermanager.domain.user.dao.UserRepository;
import picto.com.usermanager.domain.user.dto.request.EventRequest;
import picto.com.usermanager.domain.user.dto.request.UserRequest;
import picto.com.usermanager.domain.user.entity.*;

@Service
@RequiredArgsConstructor
public class UserManagerDeleteService {
    private final UserRepository userRepository;
    private final MarkRepository markRepository;
    private final BlockRepository blockRepository;

    @Transactional
    public void deleteUSer(UserRequest request){
        try {
            userRepository.delete(userRepository.getReferenceById(request.getUserId()));
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("회원 탈퇴 실패");
        }
    }

    @Transactional
    public void deleteMark(EventRequest request) throws IllegalAccessException{
        try{
            User sourceUser = userRepository.getReferenceById(request.getSourceId());
            User targetUser = userRepository.getReferenceById(request.getTargetId());
            markRepository.delete(new Mark(new MarkId(sourceUser.getUserId(), targetUser.getUserId()),targetUser, sourceUser));
        }catch (Exception e){
            throw new IllegalAccessException(e.getMessage());
        }
    }

    @Transactional
    public void deleteBlock(EventRequest request) throws IllegalAccessException{
        try{
            User sourceUser = userRepository.getReferenceById(request.getSourceId());
            User targetUser = userRepository.getReferenceById(request.getTargetId());
            blockRepository.delete(new Block(new BlockId(sourceUser.getUserId(), targetUser.getUserId()),sourceUser, targetUser));
        }catch (Exception e){
            throw new IllegalAccessException(e.getMessage());
        }
    }
}
