package picto.com.photomanager.domain.photo.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picto.com.photomanager.domain.photo.dao.PhotoRepository;

// photoStore 로 넘김
@Service
@RequiredArgsConstructor
public class PhotoManagerPostService {
    private final PhotoRepository photoRepository;

}
