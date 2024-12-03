package picto.com.photostore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import picto.com.photostore.domain.*;
import picto.com.photostore.service.PhotoService;
import picto.com.photostore.service.S3Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("photo-store/photos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class PhotoController {
    private final PhotoService photoService;
    private final S3Service s3Service;

    // 사진 업로드
    @PostMapping
    public ResponseEntity<PhotoResponse> uploadPhoto(
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "request") PhotoUploadRequest request) {
        PhotoResponse response = photoService.uploadPhoto(file, request);
        return ResponseEntity.ok(response);
    }

    // 액자로 둔 사진 업로드
    @PatchMapping("/frame/{photoId}")
    public ResponseEntity<PhotoResponse> updateFramePhoto(
            @PathVariable(name = "photoId") Long photoId,
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "request") FramePhotoUpdateRequest request) {
        PhotoResponse response = photoService.updateFramePhoto(photoId, file, request);
        return ResponseEntity.ok(response);
    }

    // 액자 목록 조회
    @GetMapping("/frames")
    public ResponseEntity<List<PhotoResponse>> getUserFramePhotos(
            @RequestParam(value = "userId") Long userId) {
        List<PhotoResponse> framePhotos = photoService.getUserFramePhotos(userId);
        return ResponseEntity.ok(framePhotos);
    }

    // 사진 공유 상태 업데이트
    @PatchMapping("/{photoId}/share")
    public ResponseEntity<PhotoResponse> updateShareStatus(
            @PathVariable(name = "photoId") Long photoId,
            @RequestParam(value = "shared")boolean shared) {
        log.info("Requested photoId: {}", photoId);
        photoService.updateShareStatus(photoId, shared);
        return ResponseEntity.ok().build();
    }

    // 사진 조회
    @GetMapping("/download/{photoId}")
    public ResponseEntity<byte[]> downloadPhoto(@PathVariable(name = "photoId") Long photoId) {
        try {
            Photo photo = photoService.getPhotoById(photoId);
            byte[] imageBytes = s3Service.downloadFile(photo.getS3FileName());
            String fileName = photo.getS3FileName();
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

            MediaType mediaType = switch (extension) {
                case "png" -> MediaType.IMAGE_PNG;
                case "gif" -> MediaType.IMAGE_GIF;
                default -> MediaType.IMAGE_JPEG;
            };

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentLength(imageBytes.length)
                    .body(imageBytes);
        } catch (Exception e) {
            log.error("이미지 다운로드 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("이미지 다운로드 실패", e);
        }
    }

    // 사진 삭제
    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(
            @PathVariable(name = "photoId") Long photoId,
            @RequestParam(value = "userId") Long userId) {
        photoService.deletePhoto(photoId, userId);
        return ResponseEntity.ok().build();
    }
}