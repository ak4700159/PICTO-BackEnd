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
            photos = photoRepository.findByUser(typeId);
        }
        // type = "photo"
        else if(type.equals("photo")){
            photos = photoRepository.findByPhoto(typeId);
        }
        else{
            throw new Exception("예상치 못한 type");
        }

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