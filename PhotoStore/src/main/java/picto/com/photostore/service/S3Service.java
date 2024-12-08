package picto.com.photostore.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.IOUtils;
import picto.com.photostore.exception.FileDeleteException;
import picto.com.photostore.exception.FileUploadException;;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.AmazonServiceException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import picto.com.photostore.exception.FileDownloadException;
import picto.com.photostore.exception.InvalidFileException;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final AmazonS3 s3client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

      // 기존 코드
//    // 사진 업로드
//    public String uploadFile(MultipartFile file) {
//        try {
//            log.info("Starting S3 file upload");
//            log.info("Original filename: {}", file.getOriginalFilename());
//            log.info("File content type: {}", file.getContentType());
//            log.info("File size: {} bytes", file.getSize());
//
//            validateFile(file);
//            String fileName = createFileName(file.getOriginalFilename());
//
//            log.info("Generated S3 filename: {}", fileName);
//
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentLength(file.getSize());
//            objectMetadata.setContentType(file.getContentType());
//            log.info("Set metadata - Content Length: {}, Content Type: {}",
//                    file.getSize(), file.getContentType());
//
//            s3client.putObject(new PutObjectRequest(
//                    bucket,
//                    fileName,
//                    file.getInputStream(),
//                    objectMetadata
//            ));
//
//            log.info("File successfully uploaded to S3: {}", fileName);
//            return fileName;
//
//        } catch (IOException e) {
//            log.error("Error during file upload: ", e);
//            throw new FileUploadException("파일 업로드 중 오류가 발생했습니다.", e);
//        }
//    }
//
//    // 사진 조회
//    public byte[] downloadFile(String fileName) {
//        try {
//            log.info("Downloading file from S3: {}", fileName);
//            var s3Object = s3client.getObject(bucket, fileName);
//            var objectInputStream = s3Object.getObjectContent();
//
//            try {
//                byte[] bytes = IOUtils.toByteArray(objectInputStream);
//                log.info("File downloaded successfully. Size: {} bytes", bytes.length);
//                return bytes;
//            } finally {
//                objectInputStream.close();
//                s3Object.close();
//            }
//        } catch (IOException e) {
//            log.error("Error downloading file from S3: ", e);
//            throw new FileUploadException("파일 다운로드 중 오류가 발생했습니다.", e);
//        }
//    }
//
//    // 사진 삭제
//    public void deleteFile(String fileName) {
//        try {
//            log.info("Deleting file from S3: {}", fileName);
//            s3client.deleteObject(bucket, fileName);
//            log.info("File deleted successfully: {}", fileName);
//        } catch (Exception e) {
//            log.error("Error deleting file from S3: ", e);
//            throw new FileDeleteException("파일 삭제 중 오류가 발생했습니다.", e);
//        }
//    }

    // 수정 코드
    public String uploadFile(MultipartFile file, Long userId, Long folderId) {
        try {
            // 파일명을 고유하게 생성
            String originalFileName = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFileName);
            String fileName = UUID.randomUUID().toString() + fileExtension;

            // 저장 경로 생성 (default 폴더를 명시적으로 생성)
            String folderPath = (folderId == null) ? "default/" : folderId.toString() + "/";
            String key = userId + "/" + folderPath + fileName;

            // S3 객체 메타데이터 설정
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            // S3에 업로드
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucket,
                    key,
                    file.getInputStream(),
                    objectMetadata
            );

            s3client.putObject(putObjectRequest);
            log.info("File uploaded successfully to path: {}", key);
            return key;
        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            throw new FileUploadException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    public byte[] downloadFile(String key) {
        try {
            S3Object s3Object = s3client.getObject(new GetObjectRequest(bucket, key));
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

            try (objectInputStream) {
                return IOUtils.toByteArray(objectInputStream);
            }
        } catch (Exception e) {
            log.error("파일 다운로드 실패: {}", e.getMessage());
            throw new FileDownloadException("파일 다운로드 중 오류가 발생했습니다.", e);
        }
    }

    public void deleteFile(String key) {
        try {
            s3client.deleteObject(bucket, key);
        } catch (AmazonServiceException e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
            throw new FileDeleteException("파일 삭제 중 오류가 발생했습니다.", e);
        }
    }

    private void validateFile(MultipartFile file) {
        log.info("Validating file");
        if (file.isEmpty()) {
            log.error("File is empty");
            throw new InvalidFileException("파일이 비어있습니다.");
        }

        String contentType = file.getContentType();
        log.info("File content type: {}", contentType);

        if (contentType == null || !contentType.startsWith("image/")) {
            log.error("Invalid content type: {}", contentType);
            throw new InvalidFileException("이미지 파일만 업로드 가능합니다.");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            log.error("File too large: {} bytes", file.getSize());
            throw new InvalidFileException("파일 크기는 10MB를 초과할 수 없습니다.");
        }
        log.info("File validation successful");
    }

    private String createFileName(String originalFileName) {
        return "picto-photos/" + UUID.randomUUID() + "_" + originalFileName;
    }

      // 기존 코드
//    public String getFileUrl(String fileName) {
//        return s3client.getUrl(bucket, fileName).toString();
//    }

    // 수정 코드
    public String getFileUrl(String key) {
        return s3client.getUrl(bucket, key).toString();
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}