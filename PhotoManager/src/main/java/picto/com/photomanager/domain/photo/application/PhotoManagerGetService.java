package picto.com.photomanager.domain.photo.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picto.com.photomanager.global.getDomain.dao.FilterRepository;
import picto.com.photomanager.global.getDomain.dao.SessionRepository;
import picto.com.photomanager.global.getDomain.dao.TagSelectRepository;
import picto.com.photomanager.domain.photo.dao.PhotoRepository;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.domain.photo.dto.request.GetSpecifiedPhotoRequest;
import picto.com.photomanager.global.getDomain.entity.Filter;
import picto.com.photomanager.global.getDomain.entity.Session;
import picto.com.photomanager.global.getDomain.entity.TagSelect;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoManagerGetService {
    private final PhotoRepository photoRepository;
    private final FilterRepository filterRepository;
    private final TagSelectRepository tagSelectRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public List<Photo> findSpecifiedPhotos(GetSpecifiedPhotoRequest request) throws IllegalAccessException, Exception {
        String type = request.getType();
        int typeId= request.getTypeId();
        List<Photo> photos;

        if(type.equals("user")){
            photos = photoRepository.findByUserID(typeId);
        }
        // type = "photo"
        else if(type.equals("photo")){
            photos = photoRepository.findByPhotoID(typeId);
        }
        else{
            throw new Exception("예상치 못한 type");
        }

        if(photos == null){
            throw new IllegalAccessException();
        }
        return photos;
    }

    @Transactional
    public List<Photo> findAroundPhotos(GetSpecifiedPhotoRequest request) throws IllegalAccessException, Exception {
        if(!request.getType().equals("user")){
            throw new IllegalAccessException();
        }
        int typeId= request.getTypeId();
        List<Photo> photos;
        
        Session userSession = sessionRepository.findById(typeId).orElseThrow();
        Filter userFilter = filterRepository.findById(typeId).orElseThrow();
        List<TagSelect> userTagSelects = tagSelectRepository.findByUserId(typeId);

        // STEP 01. 주변(10km) 사진 조회 (레파지토리에서)
        photos = photoRepository.findByLocation(userSession.getCurrentLat(), userSession.getCurrentLng());

        // STEP 02. 유저 필터에 맞는 사진 조회
        String sort = userFilter.getSort();
        String period = userFilter.getPeriod();
        Long startTime = userFilter.getStartTime();
        //02-1 sort 좋아요순 / 조회수순
        if(sort.equals("좋아요순")){
            photos.sort(new PhotoLikeComparator().reversed());
        }
        else if (sort.equals("조회수순")){
            photos.sort(new PhotoViewComparator().reversed());
        }
        else{
            throw new Exception("식별할 수 없는 sort");
        }

        //02-2 start_time 으로부터 period = 하루/일주일/한 달/ 일 년/ 사용자지정/ ALL
        if (period.equals("한 달")){

        }

        //02-3 사용자지정 시간대로 조회

        // STEP 03. 유저 태그에 맞는 사진 조회


        if(photos == null){
            throw new IllegalAccessException();
        }
        return photos;
    }


    @Transactional
    public List<Photo> findRepresentativePhotos(GetSpecifiedPhotoRequest request) throws IllegalAccessException, Exception {
        List<Photo> photos;

        if(photos == null){
            throw new IllegalAccessException();
        }
        return photos;
    }



}

class PhotoLikeComparator implements Comparator<Photo> {
    @Override
    public int compare(Photo p1, Photo p2) {
        if(p1.getLikes() > p2.getLikes()){
            return 1;
        }
        else if(p1.getLikes() < p2.getLikes()){
            return -1;
        }
        return 0;
    }
}

class PhotoViewComparator implements Comparator<Photo> {
    @Override
    public int compare(Photo p1, Photo p2) {
        if(p1.getViews() > p2.getViews()){
            return 1;
        }
        else if(p1.getViews() < p2.getViews()){
            return -1;
        }
        return 0;
    }
}