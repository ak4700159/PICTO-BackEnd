package picto.com.generator.global.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(InputStream inputStream, String originalFilename, String contentType, long size) {
        String fileName = UUID.randomUUID() + "_" + originalFilename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(size);

        amazonS3.putObject(bucket, fileName, inputStream, metadata);

        return amazonS3.getUrl(bucket, fileName).toString();
    }
}