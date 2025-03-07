package picto.com.generator.domain.user.restapi;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import picto.com.generator.domain.user.application.GeneratorPhotoService;
import picto.com.generator.domain.user.application.GeneratorUserService;
import picto.com.generator.domain.user.dto.response.GetKakaoLocationInfoResponse;
import picto.com.generator.domain.user.entity.Folder;
import picto.com.generator.domain.user.entity.Photo;
import picto.com.generator.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class GeneratorController {
    private final GeneratorUserService generatorUserService;
    private final GeneratorPhotoService generatorPhotoService;

    // 100명 유저 생성
    @PostMapping("generator/user")
    public ResponseEntity<List<User>> createTestUsers () {
        System.out.println("User created request");
        ArrayList<User> users = generatorUserService.makeUserN();
        generatorUserService.makeFilterN(users);
        generatorUserService.makeUserSettingN(users);
        generatorUserService.makeSessionN(users);
        generatorUserService.makeTagSelectN(users);
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    // 5000개 이미지 생성
    // Photo Location Folder Save Share 데이터 추가
    @PostMapping("/photo-manager/photos")
    public ResponseEntity<String> createTestPhotos(){
        final int MAX_USERS = 500;
        final int MAX_PHOTOS = 10;
        long photoCount = 1;
        for(long i = 1; i <= MAX_USERS; i++){
            Folder newFolder = generatorPhotoService.createTestFolder(i);
            for(long j = 1; j <= MAX_PHOTOS; j++){
                // i = userId
                generatorPhotoService.createTestShare(newFolder.getGeneratorId());

                // 사진을 생성하고 지역 정보 주입 후 저장한다.
                Map<String, Object> result = generatorPhotoService.createTestPhoto(i, photoCount);
                GetKakaoLocationInfoResponse info = (GetKakaoLocationInfoResponse)result.get("kakaoResponse");

                // 생성된 사진 저장, 지역정보 주입
                Photo newPhoto = (Photo)result.get("photo");
                generatorPhotoService.createTestLocationInfo(newPhoto.getUserId(), newPhoto.getPhotoId(), info);
                generatorPhotoService.createTestSave(newPhoto.getUserId(), newFolder.getFolderId(), newPhoto);

                photoCount++;
            }
        }
        return ResponseEntity.ok("good");
    }
}
