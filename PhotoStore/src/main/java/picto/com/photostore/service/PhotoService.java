package picto.com.photostore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import picto.com.photostore.domain.*;
import picto.com.photostore.exception.*;
import picto.com.photostore.repository.PhotoRepository;
import picto.com.photostore.repository.UserRepository;
import picto.com.photostore.repository.LocationInfoRepository;
import picto.com.photostore.domain.Photo;
import picto.com.photostore.domain.PhotoResponse;
import picto.com.photostore.domain.PhotoUploadRequest;
import picto.com.photostore.domain.GetKakaoLocationInfoResponse;
import picto.com.photostore.domain.GetKakaoLocationInfoResponse.Address;
import picto.com.photostore.exception.PhotoUploadException;
import picto.com.photostore.domain.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PhotoService {
    private final S3Service s3Service;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final LocationInfoRepository locationInfoRepository;
    private final SessionSchedulerClient sessionSchedulerClient;
    private static final int MAX_FRAME_PHOTOS_PER_USER = 5;

    // 사진 업로드
    public PhotoResponse uploadPhoto(MultipartFile file, PhotoUploadRequest request) {
        try {
            validateFile(file);
            validatePhotoUploadRequest(request);

            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

            // 위치 정보 조회
            String location = null;
            if (request.getLat() != 0 && request.getLng() != 0) {
                try {
                    GetKakaoLocationInfoResponse locationResponse =
                            LocationService.searchLocation(request.getLng(), request.getLat());
                    if (locationResponse.getDocuments() != null &&
                            !locationResponse.getDocuments().isEmpty()) {
                        location = createLocationString(locationResponse.getDocuments().get(0));
                    }
                } catch (Exception e) {
                    log.error("위치 정보 처리 실패", e);
                }
            }

            if (request.isFrameActive()) {
                // 프레임 사진 개수 체크
                long frameCount = photoRepository.countByUserAndFrameActiveTrue(user);
                if (frameCount >= MAX_FRAME_PHOTOS_PER_USER) {
                    throw new InvalidOperationException("프레임 사진은 최대 " + MAX_FRAME_PHOTOS_PER_USER + "개까지만 생성할 수 있습니다.");
                }

                Photo photo = Photo.builder()
                        .user(user)
                        .photoPath("temp_path")
                        .s3FileName("temp_file")
                        .lat(request.getLat())
                        .lng(request.getLng())
                        .location(location)
                        .tag(request.getTag())
                        .likes(0)
                        .views(0)
                        .registerDatetime(request.getRegisterTime())
                        .frameActive(true)
                        .sharedActive(false)
                        .build();

                return PhotoResponse.fromWithContentType(photoRepository.save(photo), file.getContentType());
            }

            // 일반 사진 업로드 처리
            String fileName = s3Service.uploadFile(file);
            String photoPath = s3Service.getFileUrl(fileName);

            Photo photo = Photo.builder()
                    .user(user)
                    .photoPath(photoPath)
                    .s3FileName(fileName)
                    .lat(request.getLat())
                    .lng(request.getLng())
                    .location(location)
                    .tag(request.getTag())
                    .likes(0)
                    .views(0)
                    .registerDatetime(request.getRegisterTime())
                    .frameActive(false)
                    .sharedActive(request.isSharedActive())
                    .build();

            Photo savedPhoto = photoRepository.save(photo);

            if (request.isSharedActive()) {
                scheduleSession(savedPhoto);
            }

            return PhotoResponse.from(savedPhoto);
        } catch (Exception e) {
            log.error("사진 업로드 실패", e);
            throw new PhotoUploadException("사진 업로드 중 오류가 발생했습니다.", e);
        }
    }

    // 액자로 둔 사진 업로드
    @Transactional
    public PhotoResponse updateFramePhoto(Long photoId, MultipartFile file,
                                          FramePhotoUpdateRequest request) {
        validateFramePhotoUpdateRequest(request);

        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException("프레임 사진을 찾을 수 없습니다."));

        if (!photo.isFrameActive()) {
            throw new InvalidOperationException("액자 사진이 아닙니다.");
        }

        validateFile(file);

        try {
            // S3에 파일 업로드
            String fileName = s3Service.uploadFile(file);
            String photoPath = s3Service.getFileUrl(fileName);

            // 위치 정보 업데이트
            String location = null;
            if (photo.getLat() != 0 && photo.getLng() != 0) {
                GetKakaoLocationInfoResponse locationResponse =
                        LocationService.searchLocation(photo.getLng(), photo.getLat());
                if (locationResponse.getDocuments() != null &&
                        !locationResponse.getDocuments().isEmpty()) {
                    location = createLocationString(locationResponse.getDocuments().get(0));
                }
            }

            photo.updatePhoto(
                    photo.getLat(),
                    photo.getLng(),
                    request.getTag(),
                    photoPath,
                    fileName,
                    false,
                    request.isSharedActive()
            );

            photo.updateLocation(location);
            photo.updateRegisterDatetime(request.getRegisterTime());

            Photo updatedPhoto = photoRepository.save(photo);

            if (request.isSharedActive()) {
                scheduleSession(updatedPhoto);
            }

            return PhotoResponse.from(updatedPhoto);
        } catch (Exception e) {
            log.error("프레임 사진 업데이트 실패", e);
            throw new PhotoUploadException("프레임 사진 업데이트 중 문제가 발생했습니다.", e);
        }
    }

    // 액자 목록 조회
    @Transactional(readOnly = true)
    public List<PhotoResponse> getUserFramePhotos(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        List<Photo> framePhotos = photoRepository.findByUserAndFrameActiveTrue(user);
        return framePhotos.stream()
                .map(PhotoResponse::from)
                .collect(Collectors.toList());
    }

    // 사진 공유 상태 업데이트
    @Transactional
    public PhotoResponse updateShareStatus(Long photoId, boolean shared) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException("사진을 찾을 수 없습니다."));

        if (photo.isFrameActive()) {
            throw new InvalidOperationException("프레임 상태의 사진은 공유 상태를 변경할 수 없습니다.");
        }

        photo.updatePhoto(
                photo.getLat(),
                photo.getLng(),
                photo.getTag(),
                photo.getPhotoPath(),
                photo.getS3FileName(),
                photo.isFrameActive(),
                shared
        );

        LocationInfo locationInfo = locationInfoRepository.findById(photoId).orElse(null);

        if (shared) {
            try {
                scheduleSession(photo);
            } catch (Exception e) {
                log.error("공유 상태 변경 중 세션 스케줄러 호출 실패", e);
                throw new SessionSchedulerException("세션 스케줄링 실패", e);
            }
        }

        return PhotoResponse.from(photoRepository.save(photo));
    }

    // 사진 조회
    @Transactional(readOnly = true)
    public Photo getPhotoById(Long photoId) {
        return photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException("사진을 찾을 수 없습니다. ID: " + photoId));
    }

    // 사진 삭제
    public void deletePhoto(Long photoId, Long userId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException("사진을 찾을 수 없습니다."));

        if (!photo.getUser().getUserId().equals(userId)) {
            throw new UnauthorizedException("해당 사진을 삭제할 권한이 없습니다.");
        }

        try {
            s3Service.deleteFile(photo.getS3FileName());
            photoRepository.delete(photo);
        } catch (Exception e) {
            log.error("사진 삭제 중 오류 발생", e);
            throw new FileDeleteException("사진 삭제 중 오류가 발생했습니다.", e);
        }
    }

    private String createLocationString(GetKakaoLocationInfoResponse.Document document) {
        if (document == null) return null;

        GetKakaoLocationInfoResponse.Address address = document.getAddress();
        if (address == null) return null;

        StringBuilder location = new StringBuilder();

        // 기본 주소
        if (address.getRegion_1depth_name() != null) {
            location.append(address.getRegion_1depth_name()).append(" ");
        }
        if (address.getRegion_2depth_name() != null) {
            location.append(address.getRegion_2depth_name()).append(" ");
        }
        if (address.getRegion_3depth_name() != null) {
            location.append(address.getRegion_3depth_name());
        }

        // 번지 주소 - 보조 번지가 있을 때만 하이픈 추가
        if (address.getMain_address_no() != null) {
            location.append(" ").append(address.getMain_address_no());
            // 보조 번지가 있고 0이 아닐 때만 하이픈과 번호 추가
            if (address.getSub_address_no() != null && !address.getSub_address_no().equals("0") && !address.getSub_address_no().isEmpty()) {
                location.append("-").append(address.getSub_address_no());
            }
        }

        return location.toString();
    }

    private void scheduleSession(Photo photo) {
        try {
            sessionSchedulerClient.scheduleSession(
                    photo.getPhotoId(),
                    photo.getUser().getUserId(),
                    photo.getLng(),
                    photo.getLat()
            );
        } catch (Exception e) {
            log.error("Session scheduler call failed", e);
            throw new SessionSchedulerException("세션 스케줄링 실패", e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("파일이 비어있습니다.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidFileException("이미지 파일만 업로드 가능합니다.");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new InvalidFileException("파일 크기는 10MB를 초과할 수 없습니다.");
        }
    }

    private void validatePhotoUploadRequest(PhotoUploadRequest request) {
        if (request.isFrameActive() && request.isSharedActive()) {
            throw new InvalidOperationException("프레임 사진인 경우 공유 상태를 활성화할 수 없습니다.");
        }
    }

    private void validateFramePhotoUpdateRequest(FramePhotoUpdateRequest request) {
        if (request.isFrameActive()) {
            throw new InvalidOperationException("프레임 사진 업로드 시에는 frameActive를 false로 설정해야 합니다.");
        }
    }
}