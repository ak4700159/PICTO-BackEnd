package picto.com.generator.global.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3 s3client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(File file, Long userId) {
        try (InputStream inputStream = new FileInputStream(file)) {
            String originalFileName = file.getName();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uuidFileName = UUID.randomUUID().toString() + fileExtension;

            String folderPath = "default/";
            String key = userId + "/" + folderPath + uuidFileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.length());
            metadata.setContentType("image/jpeg");

            s3client.putObject(bucket, key, inputStream, metadata);

            return key;
        } catch (Exception e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }
}