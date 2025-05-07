package picto.com.photostore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import picto.com.photostore.domain.photo.*;
import picto.com.photostore.exception.InvalidFileException;
import picto.com.photostore.service.PhotoService;
import picto.com.photostore.service.S3Service;

import java.util.List;

@RestController
@RequestMapping("photo-store/photos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class PhotoController {
    private final PhotoService photoService;
    private final S3Service s3Service;

    // 사진 업로드
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PhotoResponse> uploadPhoto(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "request") PhotoUploadRequest request) {

        // 일반 사진 업로드 경우에만 파일 필수 체크
        if (!request.isFrameActive() && (file == null || file.isEmpty())) {
            throw new InvalidFileException("일반 사진 업로드 시에는 파일이 필수입니다.");
        }

        PhotoResponse response = photoService.uploadPhoto(file, request);
        log.info("File upload successful. PhotoId: {}", response.getPhotoId());
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

    // 원본 사진 조회
    @GetMapping("/original/{photoId}")
    public ResponseEntity<byte[]> downloadPhoto(@PathVariable(name = "photoId") Long photoId) {
        try {
            Photo photo = photoService.getPhotoById(photoId);
            byte[] imageBytes = s3Service.downloadOriginalFile(photo.getPhotoPath());
            String fileName = photo.getPhotoPath();
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
            log.error("사진 다운로드 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("사진 다운로드 실패", e);
        }
    }

    // 리사이징 사진 조회
    @GetMapping("/resize/{photoId}")
    public ResponseEntity<byte[]> downloadResizedPhoto(
            @PathVariable(name = "photoId") Long photoId,
            @RequestParam int width,
            @RequestParam int height) {

        try {
            Photo photo = photoService.getPhotoById(photoId);
            byte[] imageBytes = s3Service.downloadResizeFile(photo.getPhotoPath(), width, height);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"resized_" + photo.getPhotoPath() + "\"")
                    .contentLength(imageBytes.length)
                    .body(imageBytes);
        } catch (Exception e) {
            log.error("사진 다운로드 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("사진 다운로드 실패", e);
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
