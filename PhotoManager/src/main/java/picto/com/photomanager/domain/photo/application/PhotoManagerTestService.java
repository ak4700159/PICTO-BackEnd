package picto.com.photomanager.domain.photo.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picto.com.photomanager.domain.photo.dao.PhotoRepository;
import picto.com.photomanager.domain.photo.dto.request.AddTestLocationInfoRequest;
import picto.com.photomanager.domain.photo.dto.request.AddTestPhotoRequest;
import picto.com.photomanager.domain.photo.dto.response.GetKakaoLocationInfoResponse;
import picto.com.photomanager.domain.photo.entity.LocationInfo;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.domain.user.dao.UserRepository;
import picto.com.photomanager.domain.user.entity.User;
import picto.com.photomanager.domain.photo.dao.LocationInfoRepository;
import picto.com.photomanager.global.postDomain.dao.FolderRepository;
import picto.com.photomanager.global.postDomain.dao.SaveRepsitory;
import picto.com.photomanager.global.postDomain.dao.ShareRepository;
import picto.com.photomanager.global.postDomain.entity.*;
import picto.com.photomanager.global.utils.KakaoUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PhotoManagerTestService {
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final LocationInfoRepository locationInfoRepository;
    private final ShareRepository shareRepository;
    private final SaveRepsitory saveRepsitory;
    private final FolderRepository folderRepository;
    private final KakaoUtils kakaoUtils;

    @Transactional
    public Map<String, Object> createTestPhoto(Long userId, Long photoId){
        Map<String, Object> result;
        Random random = new Random();

        User user = userRepository.getReferenceById(userId);
        // result = photo, kakaoResponse 키를 가지는 객체가 담긴다.
        // 대구에서 임의의 위도 경도 값(좌표) 추출 후 kakao api를 통해 지역명을 가지고 온다.
        double lat = random.nextDouble(35.88682728 - 35.77475029) + 35.77475029;
        double lng = random.nextDouble(128.6355584 - 128.4313995) +  128.4313995;
        // 밑에 문장은 비용이 많이 들 것이다... --> static function 으로 변환
        GetKakaoLocationInfoResponse kakaoResponse = kakaoUtils.convertLocationFromPos(lng, lat);
        result = new AddTestPhotoRequest().toRandomPhoto(userId, photoId, user, kakaoResponse);
        result.put("kakaoResponse", kakaoResponse);
        photoRepository.save((Photo)result.get("photo"));
        return result;
    }

    @Transactional
    public void createTestSave(Long userId, Long folderId, Photo newPhoto){
        User user = userRepository.getReferenceById(userId);
        Folder folder = folderRepository.getReferenceById(folderId);
        Save save = Save
                .builder()
                .folder(folder)
                .photo(newPhoto)
                .id(new SaveId(newPhoto.getPhotoId(), folderId))
                .savedDatetime(System.currentTimeMillis())
                .build();
        saveRepsitory.save(save);
    }

    @Transactional
    public void createTestLocationInfo(Long userId, Long photoId, GetKakaoLocationInfoResponse info){
        System.out.println(info.toString());
        Photo photo = photoRepository.getReferenceById(photoId);
        LocationInfo locationInfo = new AddTestLocationInfoRequest().toEntity(userId, photoId, info, photo);
        locationInfoRepository.save(locationInfo);
    }

    @Transactional
    public Folder createTestFolder(Long generatorId){
        // 생성자 폴더
        User generator = userRepository.getReferenceById(generatorId);
        Folder folder = Folder
                .builder()
                .generatorId(generatorId)
                .user(generator)
                .link("미구현")
                .content("폴더 설명")
                .createdDatetime(System.currentTimeMillis())
                .name("PICTO")
                .build();
        folderRepository.save(folder);
        return folder;
    }

    @Transactional
    public void createTestShare(Long userId){
        User generator = userRepository.getReferenceById(userId);
        List<Folder> folder = folderRepository.findByUserId(userId);
        Share share = Share
                .builder()
                .id(new ShareId(generator.getUserId(), folder.get(0).getFolderId()))
                .folder(folder.get(0))
                .user(generator)
                .sharedDatetime(System.currentTimeMillis())
                .build();
        shareRepository.save(share);
    }
}
