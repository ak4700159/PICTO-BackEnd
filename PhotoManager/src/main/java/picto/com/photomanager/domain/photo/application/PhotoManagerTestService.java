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
import picto.com.photomanager.domain.photo.entity.PhotoId;
import picto.com.photomanager.domain.user.dao.UserRepository;
import picto.com.photomanager.domain.user.entity.User;
import picto.com.photomanager.global.postDomain.dao.LocationInfoRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PhotoManagerTestService {
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final LocationInfoRepository locationInfoRepository;

    @Transactional
    public Map<String, Object> createTestPhoto(int userId, int photoId){
        Map<String, Object> result;
        User user = userRepository.getReferenceById(userId);
        // result = photo, kakaoResponse 키를 가지는 객체가 담긴다.
        result = new AddTestPhotoRequest().toRandomPhoto(userId, photoId, user);
        photoRepository.save((Photo)result.get("photo"));
        return result;
    }

    @Transactional
    public void createTestLocationInfo(int userId, int photoId, GetKakaoLocationInfoResponse info){
        System.out.println(info.toString());
        Photo photo = photoRepository.getReferenceById(new PhotoId(photoId, userId));
        LocationInfo locationInfo = new AddTestLocationInfoRequest().toEntity(userId, photoId, info, photo);
        locationInfoRepository.save(locationInfo);
    }
}
