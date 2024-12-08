package picto.com.photostore.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Photo", schema = "photo_schema")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long photoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "photo_path", length = 255)
    private String photoPath;

    @Column(name = "s3_file_name", length = 255)
    private String s3FileName;

    @Column(name = "tag", length = 20)
    private String tag;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;

    @Column(name = "location", length = 40)
    private String location;

    @Column(nullable = false, columnDefinition = "int = 0")
    private int likes;

    @Column(nullable = false, columnDefinition = "int = 0")
    private int views;

    @Column(name = "upload_datetime", nullable = false)
    private Long uploadDatetime;

    @Column(name = "register_datetime", nullable = false)
    private Long registerDatetime;

    @Column(name = "frame_active", nullable = false, columnDefinition = "tinyint")
    private boolean frameActive;

    @Column(name = "shared_active", nullable = false, columnDefinition = "tinyint")
    private boolean sharedActive;

    @Builder
    public Photo(Long photoId, User user, String photoPath, String s3FileName,
                 String tag, double lat, double lng, String location,
                 int likes, int views, Long registerDatetime,
                 boolean frameActive, boolean sharedActive) {
        this.photoId = photoId;
        this.user = user;
        this.photoPath = photoPath;
        this.s3FileName = s3FileName;
        this.tag = tag;
        this.lat = lat;
        this.lng = lng;
        this.location = location;
        this.likes = likes;
        this.views = views;
        this.uploadDatetime = System.currentTimeMillis();
        this.registerDatetime = registerDatetime;
        this.frameActive = frameActive;
        this.sharedActive = sharedActive;
    }

    public void updateLocation(String location) {
        this.location = location;
    }
    public void updateRegisterDatetime(Long registerDatetime) {
        this.registerDatetime = registerDatetime;
    }

    public void updatePhoto(double lat, double lng, String tag,
                            String photoPath, String s3FileName,
                            boolean frameActive, boolean sharedActive) {
        this.lat = lat;
        this.lng = lng;
        this.tag = tag;
        this.photoPath = photoPath;
        this.s3FileName = s3FileName;
        this.frameActive = frameActive;
        this.sharedActive = sharedActive;
        this.uploadDatetime = System.currentTimeMillis();
    }
}
