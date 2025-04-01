package picto.com.usermanager.domain.user.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picto.com.usermanager.domain.user.dao.*;
import picto.com.usermanager.domain.user.dto.response.get.userInfo.*;
import picto.com.usermanager.domain.user.entity.*;

import java.util.*;
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
    private final SaveRepository saveRepsitory;

    private final MarkRepository markRepository;
    private final BlockRepository blockRepository;
    private final FolderRepository folderRepository;

    // 지도 화면에서 필요한 데이터 로딩
    @Transactional
    public GetUserInfoResponse getUser(Long userId) {
        User user = userRepository.getReferenceById(userId);

        Filter filter = filterRepository.getReferenceById(user.getUserId());
        UserSetting setting = userSettingRepository.getReferenceById(user.getUserId());
        List<TagSelect> tags = tagSelectRepository.findByUserId(user.getUserId());

        // 폴더별 사진 조회
        List<Share> shares = shareRepository.findByUserId(userId);
        Map<Folder, List<Photo>> folderPhoto = new HashMap<>();
        for (Long folderId : shares.stream().map(share -> share.getId().getFolderId()).toList()) {
            List<Save> saves = saveRepsitory.getSaveByFolderId(folderId);
            List<Photo> photoList = new ArrayList<>();
            Folder folder = folderRepository.findByFolderId(folderId);
            for (Save save : saves) {
                Photo photo = photoRepository.getReferenceById(save.getId().getPhotoId());
                if (!photo.getUser().getId().equals(userId)) {
                    System.out.println("[INFO] other people photo add");
                    photoList.add(photo);
                }
            }
            folderPhoto.put(folder, photoList);
        }

        // 내 전체 사진 조회
        List<Photo> myPhotos = new ArrayList<>();
        myPhotos = photoRepository.findByUserId(userId);

        // Response entity 반환
        return GetUserInfoResponse
                .builder()
                .folderPhoto(folderPhoto)
                .user(user)
                .setting(setting)
                .filter(filter)
                .tags(tags)
                .myPhotos(myPhotos)
                .build();
    }

    @Transactional
    public GetUser GetOtherUserById(Long userId) {
        User user = userRepository.getReferenceById(userId);
        return new GetUser(user);
    }

    @Transactional
    public GetUser GetOtherUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        System.out.println(user.toString());
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
        return titles.stream().map(titleList -> new GetTitle(titleList.getTitle())).toList();
    }

    public GetTitle getTitle(Long userId) {
        TitleList titleList = titleListRepository.getReferenceById(userId);
        return new GetTitle(titleList.getTitle());
    }
}
