package picto.com.photomanager.domain.photo.restapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import picto.com.photomanager.domain.photo.application.PhotoManagerService;
import picto.com.photomanager.domain.photo.dto.PhotoEventDTO;
import picto.com.photomanager.domain.photo.dto.response.GetPhotoResponse;
import picto.com.photomanager.domain.photo.dto.request.GetAroundPhotoRequest;
import picto.com.photomanager.domain.photo.dto.request.GetRepresentativePhotoRequest;
import picto.com.photomanager.domain.photo.dto.request.GetSpecifiedPhotoRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class PhotoManagerController {
    final private PhotoManagerService photoManagerService;

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
            System.out.println(request.toString());
            photos = photoManagerService.findRepresentativePhotos(request);
        }catch (Exception e){
            System.out.println(e.getMessage());
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
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

}
