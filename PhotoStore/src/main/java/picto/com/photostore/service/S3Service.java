package picto.com.photostore.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.IOUtils;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import net.coobird.thumbnailator.Thumbnails;
import picto.com.photostore.exception.FileDeleteException;
import picto.com.photostore.exception.FileUploadException;;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.AmazonServiceException;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.io.ByteArrayOutputStream;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import picto.com.photostore.exception.FileDownloadException;
import picto.com.photostore.exception.InvalidFileException;

import javax.imageio.ImageIO;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final AmazonS3 s3client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 파일 업로드
    public String uploadFile(MultipartFile file, Long userId, Long folderId) {
        try {
            // 파일명을 고유하게 생성
            String originalFileName = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFileName);
            String fileName = UUID.randomUUID().toString() + fileExtension;

            // 저장 경로 생성 (default 폴더를 기본값으로 사용)
            String folderPath = (folderId == null) ? "default/" : folderId.toString() + "/";
            String key = userId + "/" + folderPath + fileName;

            // S3 객체 메타데이터 설정
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            // S3에 파일 업로드
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

    public BufferedImage correctOrientation(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        inputStream.transferTo(baos);
        byte[] bytes = baos.toByteArray();

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(bytes));
            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));

            if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                try {
                    int orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
                    return rotateImageByExif(image, orientation);
                } catch (MetadataException e) {
                    throw new IOException("EXIF 방향 정보를 읽는 중 오류 발생", e);
                }
            }

            return image;
        } catch (ImageProcessingException e) {
            throw new IOException("이미지 메타데이터 처리 중 오류 발생", e);
        }
    }


    public BufferedImage rotateImageByExif(BufferedImage image, int orientation) {
        int width = image.getWidth();
        int height = image.getHeight();
        AffineTransform transform = new AffineTransform();

        switch (orientation) {
            case 6: // 90도 회전
                transform.translate(height, 0);
                transform.rotate(Math.toRadians(90));
                break;
            case 3: // 180도 회전
                transform.translate(width, height);
                transform.rotate(Math.toRadians(180));
                break;
            case 8: // 270도 회전
                transform.translate(0, width);
                transform.rotate(Math.toRadians(270));
                break;
            default:
                return image; // 회전 불필요
        }

        BufferedImage rotatedImage = new BufferedImage(
                (orientation == 6 || orientation == 8) ? height : width,
                (orientation == 6 || orientation == 8) ? width : height,
                image.getType());

        Graphics2D g = rotatedImage.createGraphics();
        g.setTransform(transform);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return rotatedImage;
    }

    // 원본 사진 조회
    public byte[] downloadFile(String photoPath) {
        try {
            S3Object s3Object = s3client.getObject(new GetObjectRequest(bucket, photoPath));
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

            try (objectInputStream) {
                BufferedImage image = correctOrientation(objectInputStream);
                int width = image.getWidth();
                int height = image.getHeight();
                log.info("원본 사진 크기: {} x {}", width, height);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", baos);
                byte[] imageBytes = baos.toByteArray();
                log.info("원본 사진 용량: {} bytes (약 {} KB)", imageBytes.length, imageBytes.length / 1024);
                return imageBytes;
            }
        } catch (Exception e) {
            log.error("원본 사진 다운로드 실패: {}", e.getMessage());
            throw new FileDownloadException("사진 다운로드 중 오류가 발생했습니다.", e);
        }
    }

    // 리사이징 사진 조회
    public byte[] downloadResizeFile(String photoPath, double scale) {
        try {
            S3Object s3Object = s3client.getObject(new GetObjectRequest(bucket, photoPath));
            try (S3ObjectInputStream objectInputStream = s3Object.getObjectContent()) {
                BufferedImage originalImage = correctOrientation(objectInputStream);

                int originalWidth = originalImage.getWidth();
                int originalHeight = originalImage.getHeight();
                log.info("원본 사진 크기: {} x {}", originalWidth, originalHeight);

                int newWidth = (int) (originalWidth * scale);
                int newHeight = (int) (originalHeight * scale);
                log.info("리사이징 사진 크기: {} x {}", newWidth, newHeight);

                BufferedImage resizedImage = Thumbnails.of(originalImage)
                        .size(newWidth, newHeight)
                        .keepAspectRatio(true)
                        .asBufferedImage();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, "jpg", baos);
                byte[] imageBytes = baos.toByteArray();

                log.info("리사이징 사진 용량: {} bytes (약 {} KB)", imageBytes.length, imageBytes.length / 1024);

                return imageBytes;
            }
        } catch (Exception e) {
            log.error("리사이징 사진 다운로드 실패: {}", e.getMessage());
            throw new FileDownloadException("사진 다운로드 중 오류가 발생했습니다.", e);
        }
    }

        public void deleteFile(String photoPath) {
        try {
            s3client.deleteObject(bucket, photoPath);
        } catch (AmazonServiceException e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
            throw new FileDeleteException("파일 삭제 중 오류가 발생했습니다.", e);
        }
    }

    private void validateFile(MultipartFile file) {
        log.info("Validating file");
        if (file.isEmpty()) {
            log.error("File is empty");
            throw new InvalidFileException("파일이 비어 있습니다.");
        }

        String contentType = file.getContentType();
        log.info("File content type: {}", contentType);

        if (contentType == null || !contentType.startsWith("image/")) {
            log.error("Invalid content type: {}", contentType);
            throw new InvalidFileException("이미지 파일만 업로드 가능합니다.");
        }

        log.info("File validation successful");
    }

    // 파일 URL 반환
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