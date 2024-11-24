package picto.com.usermanager.domain.user.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picto.com.usermanager.domain.user.dao.*;
import picto.com.usermanager.domain.user.dto.response.get.userInfo.*;
import picto.com.usermanager.domain.user.entity.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserManagerGetService {
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    private final FilterRepository filterRepository;
    private final TagSelectRepositroy tagSelectRepository;
    private final UserSettingRepositroy userSettingRepository;
    private final TitleListRepository titleListRepository;

    private final ShareRepository shareRepository;
    private final FolderRepository folderRepository;

    private final MarkRepository markRepository;
    private final BlockRepository blockRepository;

    // 사용자의 모든 정보를 조회
    // = 해당 사용자의 사진, 세팅, 필터, 선택한 태그, 칭호, 정보
    @Transactional
    public GetUserInfoResponse getUser(Long userId) {
        User user = userRepository.getReferenceById(userId);

        List<Photo> photos = photoRepository.findByUserId(user.getUserId());
        Filter filter = filterRepository.getReferenceById(user.getUserId());
        UserSetting setting = userSettingRepository.getReferenceById(user.getUserId());
        List<TagSelect> tags = tagSelectRepository.findByUserId(user.getUserId());
        List<TitleList> titleList = titleListRepository.findByUserId(user.getUserId());
        List<Title> titles = titleList.stream().map(TitleList::getTitle).toList();

        List<Mark> marks = markRepository.findByUserId(user.getUserId());
        List<Block> blocks = blockRepository.findByUserId(user.getUserId());

        // 해당 유저가 속해있는 폴더 조회
        List<Folder> folders = folderRepository.findByUserId(user.getUserId());
        // 폴더별 사용자 조회
        List<Share> shares = shareRepository.findByUserId(userId);

        // Response entity 반환
        return GetUserInfoResponse
                .builder()
                .folders(folders)
                .shares(shares)
                .user(user)
                .setting(setting)
                .filter(filter)
                .tags(tags)
                .titles(titles)
                .photos(photos)
                .marks(marks)
                .blocks(blocks)
                .build();
    }

    @Transactional
    public GetUser GetOtherUser(Long userId){
        User user = userRepository.getReferenceById(userId);
        return new GetUser(user);
    }

    public GetSetting getUserSetting(Long userId) {
        UserSetting setting = userSettingRepository.getReferenceById(userId);
        return new GetSetting(setting);
    }

    public GetFilter getFilter(Long userId) {
        Filter filter = filterRepository.getReferenceById(userId);
        return new GetFilter(filter);
    }

    public List<GetTag> getTags(Long userId) {
        List<TagSelect> tags = tagSelectRepository.findByUserId(userId);
        return tags.stream().map(GetTag::new).collect(Collectors.toList());
    }

    public Set<Long> getMark(Long userId) {
        List<Mark> marks = markRepository.findByUserId(userId);
        Set<Long> markIds = new HashSet<>();
        for (Mark mark : marks) {
            markIds.add(mark.getId().getMarkedId());
        }
        return markIds;
    }

    public Set<Long> getBlock(Long userId) {
        List<Block> blocks = blockRepository.findByUserId(userId);
        Set<Long> blockIds = new HashSet<>();
        for (Block block : blocks) {
            blockIds.add(block.getBlocked().getId());
        }
        return blockIds;
    }

    public List<GetTitle> getTitleList(Long userId) {
        List<TitleList> titles = titleListRepository.findByUserId(userId);
        return titles
                .stream()
                .map(titleList -> new GetTitle(titleList.getTitle()))
                .toList();
    }

    public GetTitle getTitle(Long userId) {
        TitleList titleList = titleListRepository.getReferenceById(userId);
        return new GetTitle(titleList.getTitle());
    }
}
