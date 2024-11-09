package picto.com.photomanager.domain.photo.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picto.com.photomanager.domain.photo.dao.PhotoRepository;
import picto.com.photomanager.domain.photo.dto.request.AddTestPhotoRequest;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.global.user.dao.UserRepository;
import picto.com.photomanager.global.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoManagerTestService {
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    @Transactional
    public List<Photo> createTestPhotos(){
        List<Photo> photos = new ArrayList<>();

        int photoCount = 0;
        // 유저 100명에 대해
        for(int i = 1; i <= 100; i++){
            User user = userRepository.getReferenceById(i);
            // 각 50장씩 추가
            for(int j = 1; j <= 50; j++){
                try {
                    Thread.sleep(300);
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
                Photo newPhoto = new AddTestPhotoRequest().toRandomPhoto(i, photoCount, user);
                photoRepository.save(newPhoto);
                if(photoCount < 100){
                    photos.add(newPhoto);
                }
                photoCount++;
            }
            System.out.println("user" + i + " : 50 photos created");
        }
        return photos;
    }
}
