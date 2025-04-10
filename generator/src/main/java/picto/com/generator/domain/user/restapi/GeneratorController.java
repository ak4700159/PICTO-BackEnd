package picto.com.generator.domain.user.restapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import picto.com.generator.domain.user.application.GeneratorPhotoService;
import picto.com.generator.domain.user.application.GeneratorUserService;
import picto.com.generator.domain.user.application.LocationService;
import picto.com.generator.domain.user.dao.FolderRepository;
import picto.com.generator.domain.user.dao.PhotoRepository;
import picto.com.generator.domain.user.dao.UserRepository;
import picto.com.generator.domain.user.dto.make.MakeDefaultPhoto;
import picto.com.generator.domain.user.dto.response.GetKakaoLocationInfoResponse;
import picto.com.generator.domain.user.entity.Folder;
import picto.com.generator.domain.user.entity.Photo;
import picto.com.generator.domain.user.entity.User;
import picto.com.generator.global.utils.S3Uploader;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

@RequiredArgsConstructor
@RestController
public class GeneratorController {
    private final GeneratorUserService generatorUserService;
    private final GeneratorPhotoService generatorPhotoService;
    private final LocationService locationService;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final S3Uploader s3Uploader;

    // 유저 생성
    @PostMapping("generator/user")
    public ResponseEntity<User> createTestUser() {
        System.out.println("User created request");
        User newUser = generatorUserService.makeUser();
        generatorUserService.makeFilter(newUser);
        generatorUserService.makeUserSetting(newUser);
        generatorUserService.makeSession(newUser);
        generatorUserService.makeTagSelect(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // 3000개 이미지 생성
    // Photo Location Folder Save Share 데이터 추가
    @PostMapping("generator/photos")
    public ResponseEntity<String> createTestPhotos() {
        Long userId = 1L;
        generatorPhotoService.createTestPhotos(userId, s3Uploader, locationService);
        return ResponseEntity.ok("photos created");
    }

}