package picto.com.usermanager.domain.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import picto.com.usermanager.domain.user.dao.*;
import picto.com.usermanager.domain.user.entity.TagSelect;
import picto.com.usermanager.domain.user.entity.User;
import picto.com.usermanager.domain.user.entity.UserSetting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserManagerGetService {
    private final UserRepository userRepository;
    private final FilterRepository filterRepository;
    private final TagSelectRepositroy tagSelectRepository;
    private final UserSettingRepositroy userSettingRepository;
    private final TitleListRepository titleListRepository;

    // 사용자의 모든 정보를 조회
    // = 해당 사용자의 사진, 세팅, 필터, 선택한 태그, 칭호, 정보
    public void getUser(int userId) {

    }

    public void getUsersetting(int userId) {

    }

    public void getFilter(int userId) {

    }

    public void getTags(int userId) {

    }

    public void getMark(int userId) {

    }

    public void getBlock(int userId) {

    }

    public void getTitleList(int userId) {

    }

    public void getTitle(int userId) {

    }
}
