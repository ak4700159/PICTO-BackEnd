package picto.com.foldermanager.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import picto.com.foldermanager.domain.folder.*;
import picto.com.foldermanager.domain.notice.*;
import picto.com.foldermanager.domain.photo.*;
import picto.com.foldermanager.domain.save.*;
import picto.com.foldermanager.domain.share.*;
import picto.com.foldermanager.domain.user.*;

import picto.com.foldermanager.exception.CustomException;
import picto.com.foldermanager.exception.FolderNotFoundException;
import picto.com.foldermanager.exception.PhotoNotFoundException;

import picto.com.foldermanager.repository.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FolderService {
    private final FolderRepository folderRepository;
    private final SaveRepository saveRepository;
    private final PhotoRepository photoRepository;
    private final ShareRepository shareRepository;
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final AmazonS3 s3client;
    private final FCMService fcmService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 폴더 생성
    @Transactional
    public FolderResponse createFolder(FolderCreateRequest request) {
        User user = userRepository.findById(request.getGeneratorId())
                .orElseThrow(() -> new CustomException("해당 사용자를 찾을 수 없습니다."));

        validateFolderNameDuplicate(user, request.getName());

        // Folder 객체 생성
        Folder folder = Folder.builder()
                .generator(user)
                .name(request.getName())
                .content(request.getContent())
                .build();

        Folder savedFolder = folderRepository.save(folder);

        // Share 객체 생성
        ShareId shareId = new ShareId(user.getId(), savedFolder.getId());
        Share share = Share.builder()
                .id(shareId)
                .user(user)
                .folder(savedFolder)
                .build();

        shareRepository.save(share);

        // S3 폴더 생성
        String folderKey = createS3Key(user.getId(), savedFolder.getName(), null);
        createS3Folder(folderKey);

        return FolderResponse.from(savedFolder);
    }

    // 폴더 수정
    @Transactional
    public FolderResponse updateFolder(Long folderId, Long userId, FolderCreateRequest request) {
        Folder folder = getFolderWithAccessCheck(folderId, userId);
        validateFolderOwner(folder, userId);
        validateFolderNameDuplicate(folder.getGenerator(), request.getName());

        // 기존 S3 폴더 경로
        String oldFolderKey = createS3Key(folder.getGenerator().getId(), folder.getName(), null);

        // 새로운 S3 폴더 경로
        String newFolderKey = createS3Key(folder.getGenerator().getId(), request.getName(), null);

        renameS3Folder(oldFolderKey, newFolderKey);

        // 폴더 정보 업데이트
        folder.update(request.getName(), request.getContent());
        return FolderResponse.from(folderRepository.save(folder));
    }

    // 폴더 삭제
    @Transactional
    public void deleteFolder(Long folderId, Long userId) {
        Folder folder = getFolderWithAccessCheck(folderId, userId);
        validateFolderOwner(folder, userId);

        // S3 폴더 삭제
        String folderKey = createS3Key(folder.getGenerator().getId(), folder.getName(), null);
        deleteS3Folder(folderKey);

        saveRepository.deleteAllByFolder(folder);
        shareRepository.deleteAllByFolder(folder);
        folderRepository.delete(folder);
    }

    // 공유 폴더 초대
    @Transactional
    public ShareResponse shareFolder(ShareRequest request) {
        Folder folder = getFolderWithAccessCheck(request.getFolderId(), request.getSenderId());

        if (isDefaultFolder(folder)) {
            throw new CustomException("기본 폴더는 공유할 수 없습니다.");
        }

        User invitee = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new CustomException("초대받는 사용자를 찾을 수 없습니다."));

        if (shareRepository.existsByUserAndFolder(invitee, folder)) {
            throw new CustomException("이미 공유된 사용자입니다.");
        }

        // 초대 알림 생성 및 저장
        Notice notice = Notice.builder()
                .type(NoticeType.INVITE)
                .sender(userRepository.getReferenceById(request.getSenderId()))
                .receiver(invitee)
                .folder(folder)
                .build();

        noticeRepository.save(notice);

        // FCM 푸시 알림 전송
        if (invitee.getFcmToken() != null && !invitee.getFcmToken().isEmpty()) {
            String title = "폴더 초대 알림";
            String body = String.format("%s(%s)님이 '%s' 폴더에 초대했습니다.", notice.getSender().getName(),
                    notice.getSender().getAccountName(), folder.getName());
            fcmService.sendPushNotification(invitee.getFcmToken(), title, body);
        } else if (invitee.getFcmToken() == null || invitee.getFcmToken().isEmpty()) {
            log.info("FCM 토큰을 찾을 수 없습니다. 토큰 정보를 업데이트 해주세요.");
        }

        return null;
    }

    // 공유 폴더 알림 조회
    @Transactional(readOnly = true)
    public List<NoticeResponse> getNotices(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        List<Notice> notices = noticeRepository.findByReceiverOrderByCreatedDatetimeAsc(user);

        return notices.stream()
                .map(NoticeResponse::from)
                .collect(Collectors.toList());
    }

    // 공유 폴더 초대 수락 / 거절
    @Transactional
    public void handleNoticeAction(Long noticeId, NoticeActionRequest request) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new CustomException("알림을 찾을 수 없습니다."));

        if (!notice.getReceiver().getId().equals(request.getReceiverId())) {
            throw new CustomException("잘못된 사용자 요청입니다.");
        }

        // 초대 수락 시
        if (request.getAccept()) {
            handleAcceptNotice(notice);
        }

        noticeRepository.delete(notice);
    }

    // 초대 수락
    private void handleAcceptNotice(Notice notice) {
        // 공유된 폴더
        Folder sharedFolder = notice.getFolder();
        // 초대 받은 사용자
        User receiver = notice.getReceiver();

        // 공유 되지 않은 사용자인 경우
        if (!shareRepository.existsByUserAndFolder(receiver, sharedFolder)) {
            Share share = Share.builder()
                    .id(new ShareId(receiver.getId(), sharedFolder.getId()))
                    .user(receiver)
                    .folder(sharedFolder)
                    .build();
            shareRepository.save(share);

            String receiverFolderKey = createS3Key(receiver.getId(), sharedFolder.getName(), null);
            // 사용자 폴더 생성
            createS3Folder(receiverFolderKey);

            // 기존 파일들 복사
            String sourceFolderKey = createS3Key(sharedFolder.getGenerator().getId(), sharedFolder.getName(), null);
            copyExistingFiles(sourceFolderKey, receiverFolderKey);
        }
    }

    // 기존 폴더 파일 새로운 경로로 복사
    private void copyExistingFiles(String sourceFolderKey, String targetFolderKey) {
        try {
            // 원본 폴더 모든 파일 조회
            s3client.listObjects(bucket, sourceFolderKey)
                    .getObjectSummaries()
                    .forEach(s3Object -> {
                        if (!s3Object.getKey().endsWith("/")) {
                            String fileName = extractFileName(s3Object.getKey());
                            String targetKey = targetFolderKey + fileName;
                            copyS3File(s3Object.getKey(), targetKey);
                        }
                    });
        } catch (AmazonS3Exception e) {
            log.error("Failed to copy existing files", e);
            throw new CustomException("파일 복사 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 폴더 목록 조회
    @Transactional(readOnly = true)
    public List<ShareResponse> getSharedFolders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        List<Folder> myFolders = folderRepository.findAllByGenerator(user);
        List<Share> sharedFolders = shareRepository.findAllByUser(user);

        List<ShareResponse> responses = new ArrayList<>();
        myFolders.forEach(folder -> responses.add(ShareResponse.from(folder)));
        sharedFolders.forEach(share -> responses.add(ShareResponse.from(share)));

        return responses.stream()
                .collect(Collectors.toMap(
                        ShareResponse::getFolderId,
                        response -> response,
                        (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    // 공유 폴더 사용자 목록 조회
    @Transactional(readOnly = true)
    public List<SharedUserResponse> getSharedUsers(Long folderId, Long userId) {
        Folder folder = getFolderWithAccessCheck(folderId, userId);

        return shareRepository.findAllByFolder(folder).stream()
                .map(SharedUserResponse::from)
                .collect(Collectors.toList());
    }

    // 공유 폴더 사용자 삭제
    @Transactional
    public void removeShare(ShareRequest request) {
        Folder folder = folderRepository.findById(request.getFolderId())
                .orElseThrow(() -> new FolderNotFoundException("폴더를 찾을 수 없습니다."));

        validateFolderOwner(folder, request.getSenderId());

        // 자기 자신 삭제 방지
        if (request.getSenderId().equals(request.getReceiverId())) {
            throw new CustomException("자기 자신은 삭제할 수 없습니다.");
        }

        User user = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new CustomException("삭제 대상 사용자를 찾을 수 없습니다."));

        Share share = shareRepository.findByUserAndFolder(user, folder)
                .orElseThrow(() -> new CustomException("공유 정보를 찾을 수 없습니다."));

        // 공유 해제
        handleShareRemoval(folder, user, share);
    }

    // 공유 해제 처리
    private void handleShareRemoval(Folder folder, User user, Share share) {
        // 사용자가 업로드한 사진 처리
        List<Save> userSaves = saveRepository.findAllByFolder(folder).stream()
                .filter(save -> save.getPhoto().getUser().getUserId().equals(user.getId()))
                .collect(Collectors.toList());

        userSaves.forEach(save -> {
            String fileName = extractFileName(save.getPhoto().getPhotoPath());
            deleteFileFromSharedUsers(folder, fileName);
            saveRepository.delete(save);
        });

        // 사용자의 폴더 삭제
        String userFolderKey = createS3Key(user.getId(), folder.getName(), null);
        deleteS3Folder(userFolderKey);

        // 공유 정보 삭제
        shareRepository.delete(share);
    }

    // 폴더 사진 업로드
    @Transactional
    public SaveResponse savePhotoToFolder(Long folderId, Long photoId, Long userId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException("사진을 찾을 수 없습니다."));

        validatePhotoOwner(photo, userId);

        Folder folder = getFolderWithAccessCheck(folderId, userId);

        // default 폴더는 업로드 차단
        if (isDefaultFolder(folder)) {
            throw new CustomException("기본 폴더에는 사진을 직접 업로드할 수 없습니다.");
        }

        if (saveRepository.existsByFolderAndPhoto(folder, photo)) {
            throw new CustomException("이미 저장된 사진입니다.");
        }

        // 원본 파일 이름 추출
        String originalFileName = extractFileName(photo.getPhotoPath());
        // 공유된 사용자에게 파일 복사
        copyFileToSharedUsers(folder, originalFileName, photo.getPhotoPath());

        SaveId saveId = new SaveId(photoId, folder.getId());
        Save save = Save.builder()
                .id(saveId)
                .photo(photo)
                .folder(folder)
                .build();

        return SaveResponse.from(saveRepository.save(save));
    }

    // Default 폴더인지 확인하는 메서드
    private boolean isDefaultFolder(Folder folder) {
        return "default".equalsIgnoreCase(folder.getName());
    }

    // 폴더 사진 삭제
    @Transactional
    public void deletePhotoFromFolder(Long folderId, Long photoId, Long userId) {
        Folder folder = getFolderWithAccessCheck(folderId, userId);

        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException("사진을 찾을 수 없습니다."));

        validatePhotoOwner(photo, userId);

        Save save = saveRepository.findByFolderAndPhoto_PhotoId(folder, photoId)
                .orElseThrow(() -> new CustomException("폴더와 사진 매핑 정보를 찾을 수 없습니다."));

        // 원본 파일 이름 추출
        String originalFileName = extractFileName(photo.getPhotoPath());
        // 공유된 사용자 폴더에서 삭제
        deleteFileFromSharedUsers(folder, originalFileName);
        saveRepository.delete(save);
    }

    // 폴더별 모든 사진 조회
    @Transactional(readOnly = true)
    public List<PhotoResponse> getFolderPhotos(Long folderId, Long userId) {
        Folder folder = getFolderWithAccessCheck(folderId, userId);

        // Default 폴더의 경우
        if (isDefaultFolder(folder)) {
            return photoRepository.findByUser(folder.getGenerator()).stream()
                    .filter(photo -> !photo.isFrameActive())
                    .map(PhotoResponse::from)
                    .collect(Collectors.toList());
        }

        return saveRepository.findAllByFolder(folder).stream()
                .map(Save::getPhoto)
                .filter(photo -> !photo.isFrameActive())
                .map(PhotoResponse::from)
                .collect(Collectors.toList());
    }

    // 폴더별 특정 사진 조회
    @Transactional(readOnly = true)
    public PhotoResponse getSpecificPhotoInFolder(Long folderId, Long photoId, Long userId) {
        Folder folder = getFolderWithAccessCheck(folderId, userId);

        // Default 폴더의 경우
        if (isDefaultFolder(folder)) {
            Photo photo = photoRepository.findById(photoId)
                    .orElseThrow(() -> new PhotoNotFoundException("사진을 찾을 수 없습니다."));
            if (photo.isFrameActive()) {
                throw new CustomException("액자 사진입니다.");
            }
            return PhotoResponse.from(photo);
        }

        Save save = saveRepository.findByFolderAndPhoto_PhotoId(folder, photoId)
                .orElseThrow(() -> new PhotoNotFoundException("해당 폴더에 사진이 존재하지 않습니다."));

        Photo photo = save.getPhoto();
        if (photo.isFrameActive()) {
            throw new CustomException("액자 사진입니다.");
        }
        return PhotoResponse.from(save.getPhoto());
    }

    // -----S3 관련 Method-----

    // S3 폴더 생성
    private void createS3Folder(String folderKey) {
        try {
            // 빈 파일 생성 및 업로드
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(0);
            InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, folderKey, emptyContent, metadata);
            // S3 업로드
            s3client.putObject(putObjectRequest);
            log.info("Created S3 folder: {}", folderKey);
        } catch (Exception e) {
            log.error("Failed to create S3 folder", e);
            throw new CustomException("S3 폴더 생성 중 오류가 발생했습니다.");
        }
    }

    // S3 폴더 수정
    private void renameS3Folder(String oldFolderKey, String newFolderKey) {
        try {
            s3client.listObjects(bucket, oldFolderKey).getObjectSummaries().forEach(s3Object -> {
                String newKey = s3Object.getKey().replace(oldFolderKey, newFolderKey);
                s3client.copyObject(bucket, s3Object.getKey(), bucket, newKey);
                s3client.deleteObject(bucket, s3Object.getKey());
            });
            log.info("S3 folder renamed from {} to {}", oldFolderKey, newFolderKey);
        } catch (Exception e) {
            log.error("Failed to rename S3 folder", e);
            throw new CustomException("S3 폴더 이름 변경 중 오류가 발생했습니다.");
        }
    }

    // S3 폴더 삭제
    private void deleteS3Folder(String folderKey) {
        try {
            // 폴더 내 모든 파일 삭제
            s3client.listObjects(bucket, folderKey).getObjectSummaries()
                    .forEach(s3Object -> s3client.deleteObject(bucket, s3Object.getKey()));
            log.info("Deleted S3 folder: {}", folderKey);
        } catch (Exception e) {
            log.error("Failed to delete S3 folder", e);
            throw new CustomException("S3 폴더 삭제 중 오류가 발생했습니다.");
        }
    }

    // S3 파일 복사
    private void copyS3File(String sourceKey, String targetKey) {
        try {
            CopyObjectRequest copyRequest = new CopyObjectRequest(bucket, sourceKey, bucket, targetKey);
            // 파일 복사
            s3client.copyObject(copyRequest);
            log.info("Copied file from {} to {}", sourceKey, targetKey);
        } catch (AmazonS3Exception e) {
            log.error("Failed to copy file in S3", e);
            throw new CustomException("파일 복사 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // -----유틸리티 Method-----

    // S3 경로 생성
    private String createS3Key(Long userId, String folderName, String fileName) {
        return userId + "/" + folderName + "/" + (fileName != null ? fileName : "");
    }

    // S3 경로로부터 파일 이름 추출
    private String extractFileName(String s3FileName) {
        return s3FileName.substring(s3FileName.lastIndexOf("/") + 1);
    }

    // 공유된 사용자들에게 파일 복사
    private void copyFileToSharedUsers(Folder folder, String originalFileName, String sourceKey) {
        shareRepository.findAllByFolder(folder).forEach(share -> {
            String targetKey = createS3Key(share.getUser().getId(), folder.getName(), originalFileName);
            copyS3File(sourceKey, targetKey);
        });
    }

    // 공유된 사용자들 폴더에서 파일 삭제
    private void deleteFileFromSharedUsers(Folder folder, String fileName) {
        shareRepository.findAllByFolder(folder).forEach(share -> {
            String deleteKey = createS3Key(share.getUser().getId(), folder.getName(), fileName);
            try {
                s3client.deleteObject(bucket, deleteKey);
                log.info("Deleted file from user's folder. User ID: {}, Key: {}",
                        share.getUser().getId(), deleteKey);
            } catch (AmazonS3Exception e) {
                log.warn("Failed to delete file from S3 for user {}: {}",
                        share.getUser().getId(), e.getMessage());
            }
        });
    }

    // 폴더 접근 권한 확인한 후 폴더 객체 반환
    private Folder getFolderWithAccessCheck(Long folderId, Long userId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new FolderNotFoundException("폴더를 찾을 수 없습니다."));
        validateFolderAccess(folder, userId);
        return folder;
    }

    // -----검증 Method-----

    // 사진 소유자 체크
    private void validatePhotoOwner(Photo photo, Long userId) {
        if (!photo.getUser().getUserId().equals(userId)) {
            throw new CustomException("본인이 소유한 사진만 처리할 수 있습니다.");
        }
    }

    // 폴더 소유자 체크
    private void validateFolderOwner(Folder folder, Long userId) {
        if (!folder.getGenerator().getId().equals(userId)) {
            throw new CustomException("폴더에 대한 권한이 없습니다.");
        }
    }

    // 폴더 이름 중복 체크
    private void validateFolderNameDuplicate(User user, String folderName) {
        if (folderRepository.existsByGeneratorAndName(user, folderName)) {
            throw new CustomException("이미 동일한 이름의 폴더가 존재합니다.");
        }
    }

    // 폴더 접근 권한 체크
    private void validateFolderAccess(Folder folder, Long userId) {
        if (folder.getGenerator().getId().equals(userId)) {
            return;
        }

        boolean isSharedUser = shareRepository.existsByUserAndFolder(
                userRepository.findById(userId)
                        .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.")),
                folder);

        if (!isSharedUser) {
            throw new CustomException("해당 폴더에 대한 접근 권한이 없습니다.");
        }
    }
}