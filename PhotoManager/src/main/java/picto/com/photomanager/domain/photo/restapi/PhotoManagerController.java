package picto.com.photomanager.domain.photo.restapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import picto.com.photomanager.domain.photo.application.PhotoManagerGetService;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.domain.photo.dto.request.GetAroundPhotoRequest;
import picto.com.photomanager.domain.photo.dto.request.GetRepresentativePhotoRequest;
import picto.com.photomanager.domain.photo.dto.request.GetSpecifiedPhotoRequest;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PhotoManagerController {
    final private PhotoManagerGetService photoManagerGetService;

    // 특정 사진 조회 + 유저 포함
    @GetMapping("/photo-manager/photos")
    public ResponseEntity<List<Photo>> getSpecifiedPhotos(@RequestBody GetSpecifiedPhotoRequest request) {
        List<Photo> photos = new ArrayList<>();
        try {
            photos = photoManagerGetService.findSpecifiedPhotos(request);
        } catch (Exception e){
            System.out.println("findSpecifiedPhotos error");
        }
        return ResponseEntity.ok().body(photos);
    }
    
    // 주변 사진 조회
    @GetMapping("/photo-manager/photos/around")
    public ResponseEntity<List<Photo>> getAroundPhotos(@RequestBody GetAroundPhotoRequest request) {
        List<Photo> photos = new ArrayList<>();
        try {

        }catch (Exception e){
            System.out.println("getAroundPhotos error");
        }

        return ResponseEntity.ok().body(photos);
    }

    // 대표 사진 조회
    @GetMapping("/photo-manager/photos/representative")
    public ResponseEntity<List<Photo>> getRepresentativePhotos(@RequestBody GetRepresentativePhotoRequest request) {
        List<Photo> photos = new ArrayList<>();
        try {

        }catch (Exception e){
            System.out.println("getRepresentativePhotos error");
        }

        return ResponseEntity.ok().body(photos);
    }

}
