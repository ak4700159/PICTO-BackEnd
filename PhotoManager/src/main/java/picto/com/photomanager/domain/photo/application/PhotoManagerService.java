package picto.com.photomanager.domain.photo.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picto.com.photomanager.domain.photo.dto.request.GetAroundPhotoRequest;
import picto.com.photomanager.domain.photo.dto.request.GetRepresentativePhotoRequest;
import picto.com.photomanager.domain.photo.dto.response.GetPhotoResponse;
import picto.com.photomanager.domain.user.dao.UserRepository;
import picto.com.photomanager.domain.user.entity.User;
import picto.com.photomanager.global.getDomain.dao.FilterRepository;
import picto.com.photomanager.global.getDomain.dao.MarkRepository;
import picto.com.photomanager.global.getDomain.dao.SessionRepository;
import picto.com.photomanager.global.getDomain.dao.TagSelectRepository;
import picto.com.photomanager.domain.photo.dao.PhotoRepository;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.domain.photo.dto.request.GetSpecifiedPhotoRequest;
import picto.com.photomanager.global.getDomain.entity.Filter;
import picto.com.photomanager.global.getDomain.entity.Mark;
import picto.com.photomanager.global.getDomain.entity.Session;
import picto.com.photomanager.global.getDomain.entity.TagSelect;
import picto.com.photomanager.global.postDomain.dao.PhotoRecordRepository;
import picto.com.photomanager.global.postDomain.entity.PhotoRecord;
import picto.com.photomanager.global.postDomain.entity.PhotoRecordId;
import picto.com.photomanager.global.utils.DateUtils;
import picto.com.photomanager.global.utils.GeoDistance;
import picto.com.photomanager.global.utils.PhotoLikeComparator;
import picto.com.photomanager.global.utils.PhotoViewComparator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class PhotoManagerService {
    private final PhotoRepository photoRepository;
    private final FilterRepository filterRepository;
    private final TagSelectRepository tagSelectRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final PhotoRecordRepository photoRecordRepository;
    private final MarkRepository markRepository;
    private final DateUtils dateUtils;

    // 특정 아이디에 대한 사진 조회
    public List<GetPhotoResponse> findSpecifiedPhotos(GetSpecifiedPhotoRequest request) throws IllegalAccessException, Exception {
        String type = request.getEventType();
        Long typeId = request.getEventTypeId();
        List<Photo> photos;

        // 다른 사용자인 경우 프로필 공개 여부에 따라 조회 가능
        if ((type.equals("user") && userRepository.getReferenceById(typeId).isProfileActive()) || type.equals("owner")) {
            photos = photoRepository.findByUser(typeId);
        }
        // type = "photo"
        else if (type.equals("photo")) {
            photos = photoRepository.findByPhoto(typeId);
        } else {
            throw new Exception("예상치 못한 type");
        }


        // 다른 사용자  shared_active = true 인지 확인해야됨
        photos = type.equals("user") ? photos.stream().filter(Photo::isSharedActive).toList() : photos;

        return photos.stream().map(GetPhotoResponse::new).toList();
    }

    // 전체 사진 조회
    public List<GetPhotoResponse> findAllPhotos() {
        return photoRepository.findAll().stream().map(GetPhotoResponse::new).toList();
    }

    // 주변 사진 조회
    public List<GetPhotoResponse> findAroundPhotos(GetAroundPhotoRequest request) throws Exception {
        Long typeId = request.getSenderId();
        List<Photo> photos;
        Session userSession = sessionRepository.findById(typeId).orElseThrow();
        Filter userFilter = filterRepository.findById(typeId).orElseThrow();
        List<TagSelect> userTagSelects = tagSelectRepository.findByUserId(typeId);

        photos = photoRepository.findSharedActivePhotos();
        photos = photos
                .stream()
                .filter((photo -> GeoDistance.calculateDistance(
                        userSession.getCurrentLat(),
                        userSession.getCurrentLng(),
                        photo.getLat(),
                        photo.getLng()) < 5))
                .toList();
        String period = userFilter.getPeriod();
        long startDatetime = System.currentTimeMillis();
        long endDatetime;
        endDatetime = dateUtils.getTimeAgo(startDatetime, period);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC 기준
        photos = photos.stream().filter((photo) -> (photo.getUploadDatetime() <= startDatetime && photo.getUploadDatetime() >= endDatetime)).toList();

        List<String> tags = new ArrayList<>();
        for (TagSelect userTagSelect : userTagSelects) {
            tags.add(userTagSelect.getId().getTag());
        }
        photos = photos.stream()
                .filter((photo) -> tags.contains(photo.getTag()))
                .toList();
        photos = photos.stream().filter(Photo::isSharedActive).toList();
        List<Mark> markList = markRepository.findByUserId(typeId);
        List<Long> marks = markList.stream().map((mark -> mark.getMarked().getId())).toList();
        photos = photos.stream().filter(photo -> !marks.contains(photo.getUserId())).toList();

        return photos.stream().map(GetPhotoResponse::new).toList();
    }

    // 지역 대표 사진 조회
    public List<GetPhotoResponse> findRepresentativePhotos(GetRepresentativePhotoRequest request) throws IllegalAccessException, Exception {
        List<Photo> photos;
        Filter userFilter = filterRepository.findById(request.getSenderId()).orElseThrow();

        String eventType = request.getEventType();
        String locationName = request.getLocationName();
        String locationType = request.getLocationType();

        int count = request.getCount();
        if (locationName == null) {
            photos = switch (locationType) {
                case "large" -> photoRepository.findByTypeTopLargePhoto(request.getSenderId(), count);
                case "middle" -> photoRepository.findByTypeTopMiddlePhoto(request.getSenderId(), count);
                case "small" -> photoRepository.findByTypeTopSmallPhoto(request.getSenderId(), count);
                default -> throw new IllegalStateException("Unexpected value: " + locationType);
            };
            // 지역별 랜덤 사진 추출도 가능함
        }
        // 지역명에 대해 명시되어 있으면 해당 지역에 대해 random 또는 대표 사진을 조회
        else {
            if (eventType.equals("random")) {
                photos = photoRepository.findByRandomPhoto(locationName, count);
            } else if (eventType.equals("top")) {
                photos = switch (locationType) {
                    case "large" -> photoRepository.findByNameLargePhoto(locationName, count);
                    case "middle" -> photoRepository.findByNameMiddlePhoto(locationName, count);
                    case "small" -> photoRepository.findByNameSmallPhoto(locationName, count);
                    default -> throw new IllegalStateException("Unexpected value: " + locationType);
                };
            } else {
                throw new IllegalAccessException("not Invalid event type");
            }
        }

        List<Mark> markList = markRepository.findByUserId(request.getSenderId());
        List<Long> marks = markList.stream().map((mark -> mark.getMarked().getId())).toList();
        photos = photos.stream().filter(photo -> !marks.contains(photo.getUserId())).toList();

        String period = userFilter.getPeriod();
        long endDatetime = System.currentTimeMillis();
        long startDatetime = dateUtils.getTimeAgo(endDatetime, period);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC 기준
        photos = photos.stream().filter((photo) -> (photo.getUploadDatetime() >= startDatetime && photo.getUploadDatetime() <= endDatetime)).toList();

        return photos.stream().map(GetPhotoResponse::new).toList();
    }

    // 사용자가 사진에 좋아요를 누른 경우
    public void ClickLike(Long photoId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Photo photo = photoRepository.findById(photoId).orElseThrow();
        photo.setLikes(photo.getLikes() + 1);
        photoRepository.save(photo);

        PhotoRecord record = PhotoRecord.builder().eventDatetime(System.currentTimeMillis()).agent(user).photo(photo).type("like").id(new PhotoRecordId(userId, photoId)).build();
        photoRecordRepository.save(record);
    }

    // 사용자가 사진에 좋아요를 해제한 경우
    public void UnClickLike(Long photoId, Long userId) {
        Photo photo = photoRepository.findById(photoId).orElseThrow();
        photo.setLikes(photo.getLikes() - 1);
        photoRepository.save(photo);

        PhotoRecord record = photoRecordRepository.getReferenceById(new PhotoRecordId(userId, photoId));
        photoRecordRepository.delete(record);
    }

    public void viewPhoto(Long photoId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Photo photo = photoRepository.findById(photoId).orElseThrow();
        photo.setViews(photo.getViews() + 1);
        photoRepository.save(photo);

        PhotoRecord record = PhotoRecord.builder().eventDatetime(System.currentTimeMillis()).agent(user).photo(photo).type("view").id(new PhotoRecordId(userId, photoId)).build();
        photoRecordRepository.save(record);
    }

    public Boolean checkPhotoList(Long photoId, Long userId) {
        return photoRecordRepository.existsById(new PhotoRecordId(photoId, userId));
    }
}
