package picto.com.photomanager.domain.photo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.photomanager.global.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(schema = "photo_schema", name = "Photo")
public class Photo {
    @EmbeddedId
    private PhotoId id;

    // 복합키 안에 있는 외래키 명시
    @MapsId("userId")
    @ManyToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    // 사진 저장 경로
    @Column(name = "photo_path", nullable = true, length = 100)
    private String photoPath;

    // 위도
    @Column(name = "lat", nullable = false)
    private double lat;

    // 경도
    @Column(name = "lng", nullable = false)
    private double lng;

    // 지역명
    @Column(name = "location", nullable = false, length = 20)
    private String location;

    @Column(name = "register_datetime", nullable = false)
    private Long registerDatetime;

    @Column(name = "upload_datetime", nullable = false)
    private Long uploadDatetime;

    // 액자 활성화 여부
    @ColumnDefault("true")
    @Column(name = "frame_active", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean frameActive;

    // 사진 공유 여부
    @ColumnDefault("false")
    @Column(name = "shared_active", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean sharedActive;

    @ColumnDefault("0")
    @Column(name = "likes", nullable = false)
    private int likes;

    @ColumnDefault("0")
    @Column(name = "views", nullable = false)
    private int views;

    @Column(name = "title", length = 20)
    private String title;

    @Column(name = "tag", length = 20)
    private String tag;

    @Builder
    public Photo(User user, PhotoId photoId, String photoPath, double lat, double lng,
                 String location, Long registerDatetime, Long uploadDatetime, String tag,
                 boolean frameActive, boolean sharedActive, int likes, int views, String title) {
        this.user = user;
        this.photoPath = photoPath;
        this.lat = lat;
        this.lng = lng;
        this.location = location;
        this.registerDatetime = registerDatetime;
        this.uploadDatetime = uploadDatetime;
        this.frameActive = frameActive;
        this.sharedActive = sharedActive;
        this.likes = likes;
        this.views = views;
        this.title = title;
        this.id = photoId;
        this.tag = tag;
    }
}
