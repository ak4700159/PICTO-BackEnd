package picto.com.foldermanager.domain.photo;

import jakarta.persistence.*;
import lombok.*;
import picto.com.foldermanager.domain.user.User;

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
                 int likes, int views, boolean frameActive, boolean sharedActive) {
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
        this.registerDatetime = System.currentTimeMillis();
        this.frameActive = frameActive;
        this.sharedActive = sharedActive;
    }
}