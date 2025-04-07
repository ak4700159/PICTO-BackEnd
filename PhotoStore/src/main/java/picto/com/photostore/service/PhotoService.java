package picto.com.photostore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import picto.com.photostore.domain.folder.Folder;
import picto.com.photostore.domain.locationinfo.LocationInfo;
import picto.com.photostore.domain.photo.FramePhotoUpdateRequest;
import picto.com.photostore.domain.photo.Photo;
import picto.com.photostore.domain.photo.PhotoResponse;
import picto.com.photostore.domain.photo.PhotoUploadRequest;
import picto.com.photostore.exception.*;
import picto.com.photostore.repository.FolderRepository;
import picto.com.photostore.repository.PhotoRepository;
import picto.com.photostore.repository.UserRepository;
import picto.com.photostore.repository.LocationInfoRepository;
import picto.com.photostore.domain.locationinfo.*;
import picto.com.photostore.exception.PhotoUploadException;
import picto.com.photostore.domain.user.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PhotoService {
    private final S3Service s3Service;
    private final LocationService locationService;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final LocationInfoRepository locationInfoRepository;
    private final SessionSchedulerClient sessionSchedulerClient;

    // 액자 개수 5개로 제한
    private static final int MAX_FRAME_PHOTOS_PER_USER = 5;

    // 기본 폴더 이름 default로 설정
    private static final String DEFAULT_FOLDER_NAME = "default";

    // 사진 업로드
    @Transactional
    public PhotoResponse uploadPhoto(MultipartFile file, PhotoUploadRequest request) {
        try {
            validatePhotoUploadRequest(request);

            // 액자 사진인 경우에만 파일 검증
            if (!request.isFrameActive()) {
                validateFile(file);
            }

            // 사용자 조회
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

            // 위치 정보 조회
            String location = getLocationInfo(request.getLng(), request.getLat());

            // 일반 사진인 경우
            if (!request.isFrameActive()) {
                // default 폴더 확인 후 없을 시 생성
                Folder defaultFolder = folderRepository.findByGeneratorAndName(user, "default")
                        .orElseGet(() -> createDefaultFolder(user));
                return PhotoResponse.from(saveRegularPhoto(file, user, location, request, defaultFolder));
            }

            // 액자 사진인 경우
            return PhotoResponse.from(saveFramePhoto(user, location, request));
        } catch (Exception e) {
            log.error("사진 업로드 실패", e);
            throw new PhotoUploadException("사진 업로드 중 오류가 발생했습니다.", e);
        }
    }

    // default 폴더 생성
    private Folder createDefaultFolder(User user) {
        Folder defaultFolder = Folder.builder()
                .generator(user)
                .name(DEFAULT_FOLDER_NAME)
                .content("기본 폴더")
                .build();
        return folderRepository.save(defaultFolder);
    }

    // 액자 사진 저장 처리
    private Photo saveFramePhoto(User user, String location, PhotoUploadRequest request) {
        // 사용자별 액자 개수 제한 검사
        validateFramePhotoCount(user);

        Photo framePhoto = Photo.builder()
                .user(user)
                .photoPath("temp_path")
                .lat(request.getLat())
                .lng(request.getLng())
                .location(location)
                .tag("temp_tag")
                .likes(0)
                .views(0)
                .registerDatetime(request.getRegisterTime())
                .frameActive(true)
                .sharedActive(false)
                .build();

        return photoRepository.save(framePhoto);
    }

    // 일반 사진 저장 처리
    private Photo saveRegularPhoto(MultipartFile file, User user, String location, PhotoUploadRequest request, Folder folder) {

        String photoPath = s3Service.uploadFile(file, user.getId(),
                folder.getName().equals("default") ? null : folder.getId());

        Photo regularPhoto = Photo.builder()
                .user(user)
                .photoPath(photoPath)
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

        Photo savedPhoto = photoRepository.saveAndFlush(regularPhoto);

        if (request.isSharedActive()) {
            scheduleSessionAfterCommit(savedPhoto);
        }

        return savedPhoto;
    }

    // 사진이 DB에 정상적으로 저장된 후 스케줄링 수행
    private void scheduleSessionAfterCommit(final Photo photo) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        try {
                            scheduleSession(photo);
                        } catch (Exception e) {
                            log.error("Session scheduling failed after photo upload", e);
                        }
                    }
                });
    }

    // 액자로 둔 사진 업로드
    @Transactional
    public PhotoResponse updateFramePhoto(Long photoId, MultipartFile file, FramePhotoUpdateRequest request) {
        validateFramePhotoUpdateRequest(request);
        Photo photo = findAndValidateFramePhoto(photoId);
        validateFile(file);

        try {
            User user = photo.getUser();

            // default 폴더 확인 후 없을 시 생성
            Folder defaultFolder = folderRepository.findByGeneratorAndName(user, "default")
                    .orElseGet(() -> createDefaultFolder(user));

            Photo updatedPhoto = updatePhotoWithNewImage(photo, file, request);

            if (request.isSharedActive()) {
                scheduleSession(updatedPhoto);
            }

            return PhotoResponse.from(updatedPhoto);
        } catch (Exception e) {
            log.error("프레임 사진 업데이트 실패", e);
            throw new PhotoUploadException("프레임 사진 업데이트 중 문제가 발생했습니다.", e);
        }
    }

    // 액자 사진 새 데이터로 업데이트
    private Photo updatePhotoWithNewImage(Photo photo, MultipartFile file, FramePhotoUpdateRequest request) {
        String photoPath = s3Service.uploadFile(file, photo.getUser().getId(), null);

        photo.updatePhoto(
                photo.getLat(),
                photo.getLng(),
                request.getTag(),
                photoPath,
                false,
                request.isSharedActive()
        );

        photo.updateRegisterDatetime(request.getRegisterTime());

        return photoRepository.save(photo);
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
            s3Service.deleteFile(photo.getPhotoPath());
            photoRepository.delete(photo);

            // 사용자 default 폴더 확인
            Folder defaultFolder = folderRepository.findByGeneratorAndName(photo.getUser(), "default").orElse(null);

            if (defaultFolder != null) {
                long remainingPhotos = photoRepository.countByUserAndFrameActiveFalse(photo.getUser());

                // default 폴더 내 사진이 없다면 삭제
                if (remainingPhotos == 0) {
                    folderRepository.delete(defaultFolder);
                }
            }
        } catch (Exception e) {
            log.error("사진 삭제 중 오류 발생", e);
            throw new FileDeleteException("사진 삭제 중 오류가 발생했습니다.", e);
        }
    }

    // 위치 정보 조회
    private String getLocationInfo(double lng, double lat) {
        if (lat == 0 && lng == 0) return null;

        try {
            GetKakaoLocationInfoResponse locationResponse = locationService.searchLocation(lng, lat);
            if (locationResponse.getDocuments() != null && !locationResponse.getDocuments().isEmpty()) {
                return createLocationString(locationResponse.getDocuments().get(0));
            }
        } catch (Exception e) {
            log.error("위치 정보 처리 실패", e);
        }
        return null;
    }

    // 위치 정보 문자열 반환
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

    // 파일 유효성 검사
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("파일이 비어있습니다.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidFileException("이미지 파일만 업로드 가능합니다.");
        }
    }

    // 파일 업로드 요청 유효성 검사
    private void validatePhotoUploadRequest(PhotoUploadRequest request) {
        if (request.isFrameActive() && request.isSharedActive()) {
            throw new InvalidOperationException("프레임 사진인 경우 공유 상태를 활성화할 수 없습니다.");
        }
    }

    // 액자 개수 제한 검사
    private void validateFramePhotoCount(User user) {
        long frameCount = photoRepository.countByUserAndFrameActiveTrue(user);
        if (frameCount >= MAX_FRAME_PHOTOS_PER_USER) {
            throw new InvalidOperationException("프레임 사진은 최대 " +
                    MAX_FRAME_PHOTOS_PER_USER + "개까지만 생성할 수 있습니다.");
        }
    }

    // 액자 사진 업데이트 요청 유효성 검사
    private void validateFramePhotoUpdateRequest(FramePhotoUpdateRequest request) {
        if (request.isFrameActive()) {
            throw new InvalidOperationException("프레임 사진 업로드 시에는 frameActive를 false로 설정해야 합니다.");
        }
    }

    // 액자 사진 조회 및 검사
    private Photo findAndValidateFramePhoto(Long photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException("프레임 사진을 찾을 수 없습니다."));

        if (!photo.isFrameActive()) {
            throw new InvalidOperationException("액자 사진이 아닙니다.");
        }
        return photo;
    }
}