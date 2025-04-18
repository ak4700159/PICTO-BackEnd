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

        // Step01. 사용자인지 사진인지
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


        // Step02. 공개 여부 확인
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
        System.out.println("기초 설정");

//        // STEP 01. 주변(3km) 사진 조회 (레파지토리에서)
//        photos = photoRepository.findByLocationInfo(userSession.getCurrentLat(), userSession.getCurrentLng());
//        System.out.println("STEP 01[주변(3km) 사진 조회] size : " + photos.size());

        // STEP 01. 공유중인 사진 조회
        photos = photoRepository.findSharedActivePhotos();
        // STEP 02. 사용자 위치 기준 5km 안에 있는 검색
        photos = photos
                .stream()
                .filter((photo -> GeoDistance.calculateDistance(
                        userSession.getCurrentLat(),
                        userSession.getCurrentLng(),
                        photo.getLat(),
                        photo.getLng()) < 5))
                .toList();
        // STEP 02. 유저 필터에 맞는 사진 조회
        String sort = userFilter.getSort();
        //02-1 sort 좋아요순 / 조회수순
//        switch (sort) {
//            case "좋아요순":
//                photos.sort(new PhotoLikeComparator().reversed());
//                break;
//            case "조회수순":
//                photos.sort(new PhotoViewComparator().reversed());
//                break;
//            default:
//                throw new Exception("식별할 수 없는 sort");
//        }

        //02-2 start_time 으로부터 period = 하루/일주일/한달/일년/전체
        String period = userFilter.getPeriod();
        long startDatetime = System.currentTimeMillis();
        long endDatetime;
        endDatetime = dateUtils.getTimeAgo(startDatetime, period);

        Date startDate = new Date(startDatetime);
        Date endDate = new Date(endDatetime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC 기준

        System.out.println("startDatetime : " + sdf.format(startDate));
        System.out.println("endDatetime : " + sdf.format(endDate));
        photos = photos.stream().filter((photo) -> (photo.getUploadDatetime() <= startDatetime && photo.getUploadDatetime() >= endDatetime)).toList();
        System.out.println("STEP 02 size[유저 설정 시간, 정렬에 맞는 사진 조회] : " + photos.size());

        // STEP 03. 유저 태그에 맞는 사진 조회
        List<String> tags = new ArrayList<>();
        for (TagSelect userTagSelect : userTagSelects) {
            tags.add(userTagSelect.getId().getTag());
        }
        photos = photos.stream().filter((photo) -> {
            System.out.println("[INFO] " + photo.getTag());
            return tags.contains(photo.getTag());
        }).toList();
        System.out.println("STEP 03 size[유저 태그에 맞는 사진 조회] : " + photos.size());

        // STEP 04. 공개 여부 확인 후 반환
        photos = photos.stream().filter(Photo::isSharedActive).toList();
        System.out.println("STEP 04 size[공개 여부 확인 후 반환] : " + photos.size());

        // STEP 05. 사용자가 차단한 사용자 필터링
        List<Mark> markList = markRepository.findByUserId(typeId);
        List<Long> marks = markList.stream().map((mark -> mark.getMarked().getId())).toList();
        photos = photos.stream().filter(photo -> !marks.contains(photo.getUserId())).toList();
        System.out.println("STEP 05 size[사용자가 차단한 사용자 필터링] : " + photos.size());

        // STEP 06. RESPONSE 객체로 반환
        return photos.stream().map(GetPhotoResponse::new).toList();
    }

    // 지역 대표 사진 조회
    public List<GetPhotoResponse> findRepresentativePhotos(GetRepresentativePhotoRequest request) throws IllegalAccessException, Exception {
        List<Photo> photos;
        List<GetPhotoResponse> result;
        List<TagSelect> userTagSelects = tagSelectRepository.findByUserId(request.getSenderId());
        Filter userFilter = filterRepository.findById(request.getSenderId()).orElseThrow();

        String eventType = request.getEventType();
        String locationName = request.getLocationName();
        String locationType = request.getLocationType();

        int count = request.getCount();
        // 지역 타입에 대해 명시되어 있으면 지역별 대표 사진 조회
        // STEP 01. 지역별 대표사진 조회
        if (locationName == null) {
            System.out.println("[INFO] locationType : " + locationType);
            photos = switch (locationType) {
                case "large" -> photoRepository.findByTypeTopLargePhoto(request.getSenderId(), count);
                case "middle" -> photoRepository.findByTypeTopMiddlePhoto(count);
                case "small" -> photoRepository.findByTypeTopSmallPhoto(count);
                default -> throw new IllegalStateException("Unexpected value: " + locationType);
            };
            // 지역별 랜덤 사진 추출도 가능함
        }
        // 지역명에 대해 명시되어 있으면 해당 지역에 대해 random 또는 대표 사진을 조회
        else {
            if (eventType.equals("random")) {
                photos = photoRepository.findByRandomPhoto(locationName, count);
            } else if (eventType.equals("top")) {
                System.out.println("location name top");
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
//        photos = photoRepository.findSharedActivePhotos();


        // STEP 02. 사용자가 차단한 사용자 필터링
        List<Mark> markList = markRepository.findByUserId(request.getSenderId());
        List<Long> marks = markList.stream().map((mark -> mark.getMarked().getId())).toList();
        photos = photos.stream().filter(photo -> !marks.contains(photo.getUserId())).toList();
        System.out.println("STEP 02 size[사용자가 차단한 사용자 필터링] : " + photos.size());

        // STEP 03. 태그에 맞게 조회
        List<String> tags = new ArrayList<>();
        for (TagSelect userTagSelect : userTagSelects) {
            tags.add(userTagSelect.getId().getTag());
        }
        System.out.println("[INFO] " + tags.toString());
        photos = photos.stream().filter((photo) -> {
            System.out.println("[INFO] photo tag : " + photo.getTag());
            return tags.contains(photo.getTag());
        }).toList();
        System.out.println("STEP 03 size[사용자 태그에 맞는 사진 조회] : " + photos.size());

        // STEP 04. 필터에 맞게 조회
        String period = userFilter.getPeriod();
        System.out.println("period : " + period);
        long endDatetime = System.currentTimeMillis();
        long startDatetime;
        startDatetime = dateUtils.getTimeAgo(endDatetime, period);
        Date endDate = new Date(endDatetime);
        Date startDate = new Date(startDatetime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC 기준

        System.out.println("startDatetime : " + sdf.format(startDate));
        System.out.println("endDatetime : " + sdf.format(endDate));
        photos = photos.stream().filter((photo) -> (photo.getUploadDatetime() >= startDatetime && photo.getUploadDatetime() <= endDatetime)).toList();
        System.out.println("STEP 04 size[사용자 설정 시간에 맞게 조회] : " + photos.size());

        // STEP 05. 공유하는 사진인지 확인하고 GetPhotoResponse
        result = photos.stream().filter(Photo::isSharedActive).map(GetPhotoResponse::new).toList();
        return result;
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

    // 사용자가 선택한 기간
    public List<Photo> adaptPeriod(List<Photo> photos, Filter filter) {

        return photos;
    }
}
