package picto.com.foldermanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    // 폴더 생성
    @Transactional
    public FolderResponse createFolder(FolderCreateRequest request) {
        log.info("Creating folder with request: {}", request);

        User user = userRepository.findById(request.getGeneratorId())
                .orElseThrow(() -> new CustomException("해당 사용자를 찾을 수 없습니다."));
        log.info("Found user: {}", user);

        Folder folder = Folder.builder()
                .generator(user)
                .name(request.getName())
                .content(request.getContent())
                .build();

        Folder savedFolder = folderRepository.save(folder);
        log.info("Saved folder: {}", savedFolder);

        ShareId shareId = new ShareId(user.getId(), savedFolder.getId());
        Share share = Share.builder()
                .id(shareId)
                .user(user)
                .folder(savedFolder)
                .build();

        shareRepository.save(share);

        return FolderResponse.from(savedFolder);
    }

    // 폴더 수정
    @Transactional
    public FolderResponse updateFolder(Long folderId, Long userId, FolderCreateRequest request) {
        Folder folder = getFolderWithAccessCheck(folderId, userId);

        // 폴더 소유자 체크
        if (!folder.getGenerator().getId().equals(userId)) {
            throw new CustomException("폴더 수정 권한이 없습니다.");
        }

        folder.update(request.getName(), request.getContent());
        return FolderResponse.from(folderRepository.save(folder));
    }

    // 폴더 삭제
    @Transactional
    public void deleteFolder(Long folderId, Long userId) {
        Folder folder = getFolderWithAccessCheck(folderId, userId);

        // 폴더 소유자 체크
        if (!folder.getGenerator().getId().equals(userId)) {
            throw new CustomException("폴더 삭제 권한이 없습니다.");
        }

        saveRepository.deleteAllByFolder(folder);
        shareRepository.deleteAllByFolder(folder);
        folderRepository.delete(folder);
    }

    // 공유 폴더 초대
    @Transactional
    public ShareResponse shareFolder(ShareRequest request) {
        Folder folder = folderRepository.findById(request.getFolderId())
                .orElseThrow(() -> new FolderNotFoundException("폴더를 찾을 수 없습니다."));

        // 폴더 소유자 체크
        if (!shareRepository.existsByUserAndFolder(
                userRepository.findById(request.getSenderId())
                        .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.")),
                folder)) {
            throw new CustomException("해당 폴더에 대한 접근 권한이 없습니다.");
        }

        User invitee = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new CustomException("초대받는 사용자를 찾을 수 없습니다."));

        if (shareRepository.existsByUserAndFolder(invitee, folder)) {
            throw new CustomException("이미 공유된 사용자입니다.");
        }

        Notice notice = Notice.builder()
                .type(NoticeType.INVITE)
                .sender(userRepository.findById(request.getSenderId()).get())
                .receiver(invitee)
                .folder(folder)
                .build();

        noticeRepository.save(notice);
        return null;
    }

    // 공유 폴더 알림 조회
    @Transactional(readOnly = true)
    public List<NoticeResponse> getNotices(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        List<Notice> notices = noticeRepository.findByReceiverOrderByCreatedDatetimeAsc(user);
        if (notices.isEmpty()) {
            throw new CustomException("받은 알림이 없습니다.");
        }
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

        if (request.getAccept()) {
            ShareId shareId = new ShareId(notice.getReceiver().getId(), notice.getFolder().getId());
            Share share = Share.builder()
                    .id(shareId)
                    .user(notice.getReceiver())
                    .folder(notice.getFolder())
                    .build();
            shareRepository.save(share);
        }

        noticeRepository.delete(notice);
    }

    // 공유 폴더 목록 조회
    @Transactional(readOnly = true)
    public List<ShareResponse> getSharedFolders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        return shareRepository.findAllByUser(user).stream()
                .map(ShareResponse::from)
                .collect(Collectors.toList());
    }

    // 공유 폴더 사용자 목록 조회
    @Transactional(readOnly = true)
    public List<ShareResponse> getSharedUsers(Long folderId, Long userId) {
        Folder folder = getFolderWithAccessCheck(folderId, userId);

        return shareRepository.findAllByFolder(folder).stream()
                .map(ShareResponse::from)
                .collect(Collectors.toList());
    }

    // 공유 폴더 사용자 삭제
    @Transactional
    public void removeShare(ShareRequest request) {
        // 폴더 조회 및 접근 권한 확인
        Folder folder = folderRepository.findById(request.getFolderId())
                .orElseThrow(() -> new FolderNotFoundException("폴더를 찾을 수 없습니다."));

        // 폴더 생성자 확인
        if (!folder.getGenerator().getId().equals(request.getSenderId())) {
            throw new CustomException("폴더 생성자만 다른 사용자를 삭제할 수 있습니다.");
        }

        // 사용자 자신 삭제 방지 확인
        if (request.getSenderId().equals(request.getReceiverId())) {
            throw new CustomException("자기 자신은 삭제할 수 없습니다.");
        }

        // 삭제 대상 사용자 조회
        User user = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        // 폴더-사용자 공유 정보 조회 및 삭제
        Share share = shareRepository.findByUserAndFolder(user, folder)
                .orElseThrow(() -> new CustomException("공유 정보를 찾을 수 없습니다."));

        shareRepository.delete(share);
    }

    // 폴더 사진 업로드
    @Transactional
    public SaveResponse savePhotoToFolder(Long folderId, Long photoId, Long userId) {
        // 사진 조회
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException("사진을 찾을 수 없습니다."));

        // 사진 소유자와 userId가 동일한지 확인
        if (!photo.getUser().getUserId().equals(userId)) {
            throw new CustomException("본인이 소유한 사진만 업로드할 수 있습니다.");
        }

        // 폴더 조회
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new FolderNotFoundException("폴더를 찾을 수 없습니다."));

        // 접근 권한 확인
        validateFolderAccess(folder, userId);

        // 중복 저장 체크
        if (saveRepository.existsByFolderAndPhoto(folder, photo)) {
            throw new CustomException("이미 저장된 사진입니다.");
        }

        // SaveId 생성
        SaveId saveId = new SaveId(photoId, folder.getId());

        // Save 엔티티 생성
        Save save = Save.builder()
                .id(saveId)
                .photo(photo)
                .folder(folder)
                .build();

        return SaveResponse.from(saveRepository.save(save));
    }

    // 폴더 사진 삭제
    @Transactional
    public void deletePhotoFromFolder(Long folderId, Long photoId, Long userId) {
        // 폴더 조회
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new FolderNotFoundException("폴더를 찾을 수 없습니다."));

        // 폴더 접근 권한 확인
        validateFolderAccess(folder, userId);

        // 사진 삭제 요청한 사용자가 사진 소유자인지 확인
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException("사진을 찾을 수 없습니다."));

        if (!photo.getUser().getUserId().equals(userId)) {
            throw new CustomException("본인이 소유한 사진만 삭제할 수 있습니다.");
        }

        // 폴더와 사진 매핑 정보 확인 및 삭제
        Save save = saveRepository.findByFolderAndPhoto_PhotoId(folder, photoId)
                .orElseThrow(() -> new CustomException("폴더와 사진 매핑 정보를 찾을 수 없습니다."));

        saveRepository.delete(save);
    }

    // 폴더별 모든 사진 조회
    @Transactional(readOnly = true)
    public List<PhotoResponse> getFolderPhotos(Long folderId, Long userId) {
        Folder folder = getFolderWithAccessCheck(folderId, userId);

        return saveRepository.findAllByFolder(folder).stream()
                .map(save -> PhotoResponse.from(save.getPhoto()))
                .collect(Collectors.toList());
    }

    // 폴더별 특정 사진 조회
    @Transactional(readOnly = true)
    public PhotoResponse getSpecificPhotoInFolder(Long folderId, Long photoId, Long userId) {
        Folder folder = getFolderWithAccessCheck(folderId, userId);
        Save save = saveRepository.findByFolderAndPhoto_PhotoId(folder, photoId)
                .orElseThrow(() -> new PhotoNotFoundException("해당 폴더에 사진이 존재하지 않습니다."));

        return PhotoResponse.from(save.getPhoto());
    }

    private Folder getFolderWithAccessCheck(Long folderId, Long userId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new FolderNotFoundException("폴더를 찾을 수 없습니다."));

        validateFolderAccess(folder, userId);
        return folder;
    }

    private void validateFolderAccess(Folder folder, Long userId) {
        if (!shareRepository.existsByUserAndFolder(
                userRepository.findById(userId)
                        .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.")),
                folder)) {
            throw new CustomException("해당 폴더에 대한 접근 권한이 없습니다.");
        }
    }
}