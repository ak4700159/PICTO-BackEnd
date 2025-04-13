package picto.com.foldermanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import picto.com.foldermanager.domain.folder.FolderCreateRequest;
import picto.com.foldermanager.domain.folder.FolderResponse;
import picto.com.foldermanager.domain.notice.NoticeActionRequest;
import picto.com.foldermanager.domain.notice.NoticeResponse;
import picto.com.foldermanager.domain.photo.PhotoResponse;
import picto.com.foldermanager.domain.save.SaveResponse;
import picto.com.foldermanager.domain.share.ShareRequest;
import picto.com.foldermanager.domain.share.ShareResponse;
import picto.com.foldermanager.exception.CustomException;
import picto.com.foldermanager.service.FolderService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("folder-manager/folders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FolderController {
    private final FolderService folderService;

    // 폴더 생성
    @PostMapping
    public ResponseEntity<FolderResponse> createFolder(@RequestBody FolderCreateRequest request) {
        return ResponseEntity.ok(folderService.createFolder(request));
    }

    // 폴더 수정
    @PatchMapping("/{folderId}")
    public ResponseEntity<FolderResponse> updateFolder(
            @PathVariable Long folderId,
            @RequestParam Long generatorId,
            @RequestBody FolderCreateRequest request) {
        return ResponseEntity.ok(folderService.updateFolder(folderId, generatorId, request));
    }

    // 폴더 삭제
    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(
            @PathVariable Long folderId,
            @RequestParam Long generatorId) {
        folderService.deleteFolder(folderId, generatorId);
        return ResponseEntity.ok().build();
    }

    // 공유 폴더 초대
    @PostMapping("/shares")
    public ResponseEntity<ShareResponse> shareFolder(@RequestBody ShareRequest request) {
        return ResponseEntity.ok(folderService.shareFolder(request));
    }

    // 공유 폴더 알림 조회
    @GetMapping("/shares/notices")
    public ResponseEntity<List<NoticeResponse>> getNotices(@RequestParam Long receiverId) {
        return ResponseEntity.ok(folderService.getNotices(receiverId));
    }

    // 공유 폴더 초대 수락 / 거절
    @PostMapping("/shares/notices/{noticeId}")
    public ResponseEntity<Void> handleNoticeAction(
            @PathVariable Long noticeId,
            @RequestBody NoticeActionRequest request) {
        folderService.handleNoticeAction(noticeId, request);
        return ResponseEntity.ok().build();
    }

    // 폴더 목록 조회
    @GetMapping("/shares/users/{userId}")
    public ResponseEntity<List<ShareResponse>> getSharedFolders(
            @PathVariable Long userId) {
        return ResponseEntity.ok(folderService.getSharedFolders(userId));
    }

    // 공유 폴더 사용자 목록 조회
    @GetMapping("/shares/{folderId}")
    public ResponseEntity<List<ShareResponse>> getSharedUsers(
            @PathVariable Long folderId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(folderService.getSharedUsers(folderId, userId));
    }

    // 공유 폴더 사용자 삭제
    @DeleteMapping("/shares")
    public ResponseEntity<Void> removeShare(@RequestBody ShareRequest request) {
        folderService.removeShare(request);
        return ResponseEntity.ok().build();
    }

    // 폴더 사진 업로드
    @PostMapping("/{folderId}/photos/upload")
    public ResponseEntity<SaveResponse> uploadPhotoToFolder(
            @PathVariable Long folderId,
            @RequestParam Long photoId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(folderService.savePhotoToFolder(folderId, photoId, userId));
    }

    // 폴더 사진 삭제
    @DeleteMapping("/{folderId}/photos/{photoId}")
    public ResponseEntity<Void> deletePhotoFromFolder(
            @PathVariable Long folderId,
            @PathVariable Long photoId,
            @RequestParam Long userId) {
        folderService.deletePhotoFromFolder(folderId, photoId, userId);
        return ResponseEntity.ok().build();
    }

    // 폴더별 모든 사진 조회
    @GetMapping("/{folderId}/photos")
    public ResponseEntity<List<PhotoResponse>> getAllPhotosInFolder(
            @PathVariable Long folderId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(folderService.getFolderPhotos(folderId, userId));
    }

    // 폴더별 특정 사진 조회
    @GetMapping("/{folderId}/photos/{photoId}")
    public ResponseEntity<PhotoResponse> getPhotoInFolder(
            @PathVariable Long folderId,
            @PathVariable Long photoId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(folderService.getSpecificPhotoInFolder(folderId, photoId, userId));
    }
}