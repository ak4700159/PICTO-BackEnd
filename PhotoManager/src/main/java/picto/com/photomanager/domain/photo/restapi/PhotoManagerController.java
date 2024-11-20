package picto.com.photomanager.domain.photo.restapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import picto.com.photomanager.domain.photo.application.PhotoManagerGetService;
import picto.com.photomanager.domain.photo.application.PhotoManagerTestService;
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
    final private PhotoManagerGetService photoManagerGetService;
    final private PhotoManagerTestService photoManagerTestService;

    // 5000개 이미지 생성
    @PostMapping("/photo-manager/photos")
    public ResponseEntity<String> createTestPhoto(){
        final int MAX_USERS = 500;
        final int MAX_PHOTOS = 10;
        long photoCount = 1;
        for(long i = 1; i <= MAX_USERS; i++){
            for(long j = 1; j <= MAX_PHOTOS; j++){
                // 폴더를 생성하고 생성자를 공유한다.(매핑)
                Folder newFolder = photoManagerTestService.createTestFolder(i, i);
                photoManagerTestService.createTestShare(newFolder.getId());

                // 사진을 생성하고 지역 정보 주입 후 저장한다.
                Map<String, Object> result = photoManagerTestService.createTestPhoto(i, photoCount);
                GetKakaoLocationInfoResponse info = (GetKakaoLocationInfoResponse)result.get("kakaoResponse");
                photoManagerTestService.createTestLocationInfo(i, photoCount, info);
                photoManagerTestService.createTestSave(i, newFolder.getId(), (Photo)result.get("photo"));

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
            photos = photoManagerGetService.findSpecifiedPhotos(request);
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
            photos = photoManagerGetService.findAroundPhotos(request);
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
            photos = photoManagerGetService.findRepresentativePhotos(request);
        }catch (Exception e){
            System.out.println("getRepresentativePhotos error");
        }

        return ResponseEntity.ok().body(photos);
    }
}
