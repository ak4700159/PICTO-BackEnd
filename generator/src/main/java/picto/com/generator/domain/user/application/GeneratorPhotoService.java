package picto.com.generator.domain.user.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import picto.com.generator.domain.user.dao.*;
import picto.com.generator.domain.user.dto.make.MakeDefaultLocationInfo;
import picto.com.generator.domain.user.dto.make.MakeDefaultPhoto;
import picto.com.generator.domain.user.dto.response.GetKakaoLocationInfoResponse;
import picto.com.generator.domain.user.entity.*;
import picto.com.generator.global.utils.S3Uploader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GeneratorPhotoService {
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final ShareRepository shareRepository;
    private final SaveRepsitory saveRepsitory;
    private final FolderRepository folderRepository;
    private final LocationInfoRepository locationInfoRepository;

    @Transactional
    public void createTestPhotos(Long userId, S3Uploader s3Uploader, LocationService locationService) {
        String[] tags = {"개", "고양이", "새"};
        long photoCount = 1L;

        Folder folder = createTestFolder(userId);
        createTestShare(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Random random = new Random();

        for (String tag : tags) {
            File imageDir = new File("src/main/resources/static/" + tag);
            File[] imageFiles = imageDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));

            if (imageFiles == null || imageFiles.length == 0) {
                throw new RuntimeException(tag + " 이미지 파일 끝");
            }

            Arrays.sort(imageFiles);
            int uploadCount = imageFiles.length;

            for (int i = 0; i < uploadCount; i++) {
                File imageFile = imageFiles[i];

                double lat = 0.0;
                double lng = 0.0;
                String location = "좌표 식별 불가";
                GetKakaoLocationInfoResponse kakaoResponse = null;

                while (location.equals("좌표 식별 불가")) {
                    if (photoCount % 20 == 0) {
                        // 제주도 범위
                        lat = random.nextDouble(33.55321855 - 33.11130278) + 33.11130278;
                        lng = random.nextDouble(126.95101599 - 126.14484406) + 126.14484406;
                    } else {
                        // 제주도 제외 범위
                        lat = random.nextDouble(38.61244627 - 34.22222699) + 34.22222699;
                        lng = random.nextDouble(129.58312748 - 126.10356267) + 126.10356267;
                    }

                    kakaoResponse = locationService.searchLocation(lng, lat);
                    if (kakaoResponse != null && !kakaoResponse.getDocuments().isEmpty()) {
                        location = kakaoResponse.getDocuments().get(0).getAddress().getAddress_name();
                    }
                }

                Map<String, Object> result = new MakeDefaultPhoto().toRandomPhoto(userId, photoCount, user, kakaoResponse, lat, lng, tag);
                Photo newPhoto = (Photo) result.get("photo");
                newPhoto.setTag(tag);

                try (InputStream inputStream = new FileInputStream(imageFile)) {
                    String photoPath = s3Uploader.upload(imageFile, user.getUserId());
                    newPhoto.setPhotoPath(photoPath);
                } catch (Exception e) {
                    throw new RuntimeException("S3 업로드 실패: " + imageFile.getName(), e);
                }

                photoRepository.save(newPhoto);
                createTestLocationInfo(userId, newPhoto.getPhotoId(), kakaoResponse);
                createTestSave(userId, folder.getFolderId(), newPhoto);

                photoCount++;
            }
        }
    }

    @Transactional
    public void createTestSave(Long userId, Long folderId, Photo newPhoto){
        User user = userRepository.getReferenceById(userId);
        Folder folder = folderRepository.getReferenceById(folderId);
        Save save = Save
                .builder()
                .folder(folder)
                .photo(newPhoto)
                .id(new SaveId(newPhoto.getPhotoId(), folderId))
                .savedDatetime(System.currentTimeMillis())
                .build();
        saveRepsitory.save(save);
    }

    @Transactional
    public void createTestLocationInfo(Long userId, Long photoId, GetKakaoLocationInfoResponse info){
        System.out.println(info.toString());
        Photo photo = photoRepository.getReferenceById(photoId);
        LocationInfo locationInfo = new MakeDefaultLocationInfo().toEntity(userId, photoId, info, photo);
        locationInfoRepository.save(locationInfo);
    }

    @Transactional
    public Folder createTestFolder(Long generatorId){
        // 생성자 폴더
        User generator = userRepository.getReferenceById(generatorId);
        Folder folder = Folder
                .builder()
                .generatorId(generatorId)
                .user(generator)
                .content("폴더 설명")
                .createdDatetime(System.currentTimeMillis())
                .name("PICTO")
                .build();
        folderRepository.save(folder);
        return folder;
    }

    @Transactional
    public void createTestShare(Long userId){
        User generator = userRepository.getReferenceById(userId);
        List<Folder> folder = folderRepository.findByUser(generator);
        Share share = Share
                .builder()
                .id(new ShareId(generator.getUserId(), folder.get(0).getFolderId()))
                .folder(folder.get(0))
                .user(generator)
                .sharedDatetime(System.currentTimeMillis())
                .build();
        shareRepository.save(share);
    }
}
