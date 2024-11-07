package picto.com.photomanager.domain.photo.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picto.com.photomanager.domain.photo.dto.request.GetAroundPhotoRequest;
import picto.com.photomanager.domain.photo.dto.request.GetRepresentativePhotoRequest;
import picto.com.photomanager.domain.photo.dto.response.GetPhotoResponse;
import picto.com.photomanager.global.getDomain.dao.FilterRepository;
import picto.com.photomanager.global.getDomain.dao.SessionRepository;
import picto.com.photomanager.global.getDomain.dao.TagSelectRepository;
import picto.com.photomanager.domain.photo.dao.PhotoRepository;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.domain.photo.dto.request.GetSpecifiedPhotoRequest;
import picto.com.photomanager.global.getDomain.entity.Filter;
import picto.com.photomanager.global.getDomain.entity.Session;
import picto.com.photomanager.global.getDomain.entity.TagSelect;
import picto.com.photomanager.global.utils.DateUtils;
import picto.com.photomanager.global.utils.PhotoLikeComparator;
import picto.com.photomanager.global.utils.PhotoViewComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoManagerGetService {
    private final PhotoRepository photoRepository;
    private final FilterRepository filterRepository;
    private final TagSelectRepository tagSelectRepository;
    private final SessionRepository sessionRepository;

    // 특정 아이디에 대한 사진 조회
    @Transactional
    public List<GetPhotoResponse> findSpecifiedPhotos(GetSpecifiedPhotoRequest request) throws IllegalAccessException, Exception {
        String type = request.getType();
        int typeId= request.getTypeId();
        List<Photo> photos;

        // Step01. 사용자인지 사진인지
        if(type.equals("user")){
            photos = photoRepository.findByUser(typeId);
        }
        // type = "photo"
        else if(type.equals("photo")){
            photos = photoRepository.findByPhoto(typeId);
        }
        else{
            throw new Exception("예상치 못한 type");
        }

        // Step02. 공개 여부 확인
        // shared_active = true 인지 확인해야됨
        photos = photos.stream().filter(Photo::isSharedActive).toList();

        return photos.stream().map(GetPhotoResponse::new).toList();
    }

    // 주변 사진 조회
    @Transactional
    public List<GetPhotoResponse> findAroundPhotos(GetAroundPhotoRequest request) throws IllegalAccessException, Exception {
        if(!request.getType().equals("user")){
            throw new IllegalAccessException();
        }
        int typeId= request.getTypeId();
        List<Photo> photos;

        Session userSession = sessionRepository.findById(typeId).orElseThrow();
        Filter userFilter = filterRepository.findById(typeId).orElseThrow();
        List<TagSelect> userTagSelects = tagSelectRepository.findByUserId(typeId);

        // STEP 01. 주변(10km) 사진 조회 (레파지토리에서)
        photos = photoRepository.findByLocationInfo(userSession.getCurrentLat(), userSession.getCurrentLng());

        // STEP 02. 유저 필터에 맞는 사진 조회
        String sort = userFilter.getSort();
        //02-1 sort 좋아요순 / 조회수순
        switch (sort){
            case "좋아요순" : photos.sort(new PhotoLikeComparator().reversed()); break;
            case "조회수순" : photos.sort(new PhotoViewComparator().reversed()); break;
            default: throw new Exception("식별할 수 없는 sort");
        }

        //02-2 start_time 으로부터 period = 하루/일주일/한 달/일 년/ 사용자지정/ ALL
        String period = userFilter.getPeriod();
        long startDate = userFilter.getStartDateTime();
        long endDate;
        if(period.equals("사용자지정")){
            endDate = userFilter.getEndDateTime();
        }
        else{
            endDate = DateUtils.getTimeAgo(startDate, period);
        }

        photos = photos
                .stream()
                .filter((photo)-> photo.getUploadDatetime() >= startDate
                        && photo.getUploadDatetime() <= endDate)
                .toList();

        // STEP 03. 유저 태그에 맞는 사진 조회
        List<String> tags = new ArrayList<>();
        for (TagSelect userTagSelect : userTagSelects) {
            tags.add(userTagSelect.getId().getTag());
        }
        photos = photos
                .stream()
                .filter((photo) -> tags.contains(photo.getTag()))
                .toList();

        // STEP 04. 공개 여부 확인
        photos = photos.stream().filter(Photo::isSharedActive).toList();

        return photos.stream().map(GetPhotoResponse::new).toList();
    }

    // 지역 대표 사진 조회
    @Transactional
    public List<GetPhotoResponse> findRepresentativePhotos(GetRepresentativePhotoRequest request) throws IllegalAccessException, Exception {
        List<Photo> photos;

        String eventType = request.getEventType();
        String locationName = request.getLocationName();
        String locationType = request.getLocationType();
        int count = request.getCount();
        // 지역명에 값이 있는 경우 해당 지역명에 대해 조회
        photos = switch (eventType) {
            case "random" -> photoRepository.findByRandomPhoto(locationName != null ? locationName : locationType, count);
            case "top" -> photoRepository.findByTopPhoto(locationName != null ? locationName : locationType, count);
            default -> throw new IllegalAccessException();
        };


        if(photos == null){
            throw new IllegalAccessException();
        }

        return photos.stream().map(GetPhotoResponse::new).toList();
    }
}
