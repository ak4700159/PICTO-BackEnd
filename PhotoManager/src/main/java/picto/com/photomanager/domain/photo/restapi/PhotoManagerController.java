package picto.com.photomanager.domain.photo.restapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import picto.com.photomanager.domain.photo.application.PhotoManagerService;
import picto.com.photomanager.domain.photo.application.PhotoManagerTestService;
import picto.com.photomanager.domain.photo.dto.PhotoEventDTO;
import picto.com.photomanager.domain.photo.dto.response.GetKakaoLocationInfoResponse;
import picto.com.photomanager.domain.photo.dto.response.GetPhotoResponse;
import picto.com.photomanager.domain.photo.dto.request.GetAroundPhotoRequest;
import picto.com.photomanager.domain.photo.dto.request.GetRepresentativePhotoRequest;
import picto.com.photomanager.domain.photo.dto.request.GetSpecifiedPhotoRequest;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.global.postDomain.entity.Folder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class PhotoManagerController {
    final private PhotoManagerService photoManagerService;
    final private PhotoManagerTestService photoManagerTestService;

    // 5000개 이미지 생성
    // Photo Location Folder Save Share 데이터 추가
    @PostMapping("/photo-manager/photos")
    public ResponseEntity<String> createTestPhoto(){
        final int MAX_USERS = 500;
        final int MAX_PHOTOS = 10;
        long photoCount = 1;
        for(long i = 1; i <= MAX_USERS; i++){
            Folder newFolder = photoManagerTestService.createTestFolder(i);
            for(long j = 1; j <= MAX_PHOTOS; j++){
                // 폴더를 생성하고 생성자를 공유한다.(매핑)
                // i = userId
                photoManagerTestService.createTestShare(newFolder.getGeneratorId());

                // 사진을 생성하고 지역 정보 주입 후 저장한다.
                Map<String, Object> result = photoManagerTestService.createTestPhoto(i, photoCount);
                GetKakaoLocationInfoResponse info = (GetKakaoLocationInfoResponse)result.get("kakaoResponse");

                // 생성된 사진 활용
                Photo newPhoto = (Photo)result.get("photo");
                photoManagerTestService.createTestLocationInfo(newPhoto.getUserId(), newPhoto.getPhotoId(), info);
                photoManagerTestService.createTestSave(newPhoto.getUserId(), newFolder.getFolderId(), newPhoto);

                photoCount++;
            }
        }
        return ResponseEntity.ok("good");
    }


    // 특정 사진 조회 + 유저 포함
    @GetMapping("/photo-manager/photos")
    public ResponseEntity<List<GetPhotoResponse>> getSpecifiedPhotos(@RequestBody GetSpecifiedPhotoRequest request) {
        List<GetPhotoResponse> photos = new ArrayList<>();
        try {
            photos = photoManagerService.findSpecifiedPhotos(request);
        } catch (Exception e){
            System.out.println("findSpecifiedPhotos error");
        }
        return ResponseEntity.ok().body(photos);
    }
    
    // 주변 사진 조회
    @GetMapping("/photo-manager/photos/around")
    public ResponseEntity<List<GetPhotoResponse>> getAroundPhotos(@RequestBody GetAroundPhotoRequest request) {
        List<GetPhotoResponse> photos = new ArrayList<>();
        try {
            photos = photoManagerService.findAroundPhotos(request);
        }catch (Exception e){
            System.out.println("getAroundPhotos error");
        }

        return ResponseEntity.ok().body(photos);
    }

    // 대표 사진 조회
    @GetMapping("/photo-manager/photos/representative")
    public ResponseEntity<List<GetPhotoResponse>> getRepresentativePhotos(@RequestBody GetRepresentativePhotoRequest request) {
        List<GetPhotoResponse> photos = new ArrayList<>();
        try {
            photos = photoManagerService.findRepresentativePhotos(request);
        }catch (Exception e){
            System.out.println("getRepresentativePhotos error");
            return ResponseEntity.badRequest().body(photos);
        }

        return ResponseEntity.ok().body(photos);
    }

    @PostMapping("/photo-manager/photos/like")
    public ResponseEntity<?> clickLike(@RequestBody PhotoEventDTO eventDTO){
        try {
            photoManagerService.ClickLike(eventDTO.getPhotoId(), eventDTO.getUserId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/photo-manager/photos/unlike")
    public ResponseEntity<?> unClickLike(@RequestBody PhotoEventDTO eventDTO){
        try {
            photoManagerService.UnClickLike(eventDTO.getPhotoId(), eventDTO.getUserId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/photo-manager/photos/view")
    public ResponseEntity<?> clickPhoto(@RequestBody PhotoEventDTO eventDTO){
        try{
            photoManagerService.viewPhoto(eventDTO.getPhotoId(), eventDTO.getUserId());
        } catch (Exception e){
            System.out.println("viewPhoto error");
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

}
