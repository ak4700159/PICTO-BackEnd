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
import java.util.UUID;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import picto.com.photostore.exception.FileDownloadException;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final AmazonS3 s3client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // S3에 파일 업로드
    public String uploadFile(MultipartFile file) {
        try {
            // 파일명을 고유하게 생성 (디렉토리 경로 + UUID + 원본 파일명)
            String fileName = createFileName(file.getOriginalFilename());

            // S3 객체 메타데이터 설정 (파일 크기와 타입)
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            // S3 업로드 요청 객체 생성
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucket,
                    fileName,
                    file.getInputStream(),
                    objectMetadata
            );

            // S3에 파일 업로드 요청
            s3client.putObject(putObjectRequest);

            // 업로드된 파일 이름 반환
            return fileName;

        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            throw new FileUploadException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    // 사진 조회
    public byte[] downloadFile(String fileName) {
        try {
            // S3 파일 객체 가져오기
            S3Object s3Object = s3client.getObject(new GetObjectRequest(bucket, fileName));
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

            try {
                // InputStream을 바이트 배열로 변환
                byte[] bytes = IOUtils.toByteArray(objectInputStream);
                return bytes;
            } catch (IOException e) {
                log.error("파일 스트림 읽기 실패: {}", e.getMessage());
                throw new FileDownloadException("파일 다운로드 중 오류가 발생했습니다.", e);
            } finally {
                try {
                    objectInputStream.close();
                    s3Object.close();
                } catch (IOException e) {
                    log.warn("스트림 닫기 실패: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("S3에서 파일 다운로드 실패: {}", e.getMessage());
            throw new FileDownloadException("S3에서 파일 다운로드 중 오류가 발생했습니다.", e);
        }
    }

    // S3에 파일 삭제
    public void deleteFile(String fileName) {
        try {
            // S3에 파일 삭제 요청
            s3client.deleteObject(bucket, fileName);

        } catch (AmazonServiceException e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
            throw new FileDeleteException("파일 삭제 중 오류가 발생했습니다.", e);
        }
    }

    // 업로드된 파일의 URL 반환
    public String getFileUrl(String fileName) {
        return s3client.getUrl(bucket, fileName).toString();
    }

    // 업로드 파일의 고유 이름 생성
    private String createFileName(String originalFileName) {
        return "picto-photos/" + UUID.randomUUID().toString() + "_" + originalFileName;
    }
}