package picto.com.usermanager.domain.user.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picto.com.usermanager.domain.user.dao.*;
import picto.com.usermanager.domain.user.dto.response.get.userInfo.GetUserInfoResponse;
import picto.com.usermanager.domain.user.entity.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagerGetService {
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final FilterRepository filterRepository;
    private final TagSelectRepositroy tagSelectRepository;
    private final UserSettingRepositroy userSettingRepository;
    private final TitleListRepository titleListRepository;

    private final MarkRepository markRepository;
    private final BlockRepository blockRepository;

    // 사용자의 모든 정보를 조회
    // = 해당 사용자의 사진, 세팅, 필터, 선택한 태그, 칭호, 정보
    @Transactional
    public GetUserInfoResponse getUser(int userId) {
        User user = userRepository.getReferenceById(userId);

        List<Photo> photos = photoRepository.findByUserId(user.getUserId());
        Filter filter = filterRepository.getReferenceById(user.getUserId());
        UserSetting setting = userSettingRepository.getReferenceById(user.getUserId());
        List<TagSelect> tags = tagSelectRepository.findByUserId(user.getUserId());
        List<TitleList> titleList = titleListRepository.findByUserId(user.getUserId());
        List<Title> titles = titleList.stream().map(TitleList::getTitle).toList();

        List<Mark> marks = markRepository.findByUserId(user.getUserId());
        List<Block> blocks = blockRepository.findByUserId(user.getUserId());

        // Response entity 반환
        GetUserInfoResponse response = GetUserInfoResponse
                .builder()
                .user(user)
                .setting(setting)
                .filter(filter)
                .tags(tags)
                .titles(titles)
                .photos(photos)
                .marks(marks)
                .blocks(blocks)
                .build();
        return response;
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
