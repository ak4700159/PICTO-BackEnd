package picto.com.photomanager.domain.photo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.photomanager.domain.photo.dto.PhotoDistanceDTO;
import picto.com.photomanager.domain.user.entity.User;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(schema = "photo_schema", name = "Photo")
public class Photo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id", nullable = false)
    private Long photoId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 복합키 안에 있는 외래키 명시
    @MapsId("userId")
    @ManyToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    // user 내용만 제외한 정보들만을 serialize하게 된다. -> json 오류 방지
    @JsonIgnore
    private User user;

    // 사진 저장 경로
    @Column(name = "photo_path", nullable = true, length = 255)
    private String photoPath;

    @Column(name = "s3_file_name", nullable = true, length = 255)
    private String s3FileName;

    // 위도
    @Column(name = "lat", nullable = false)
    private double lat;

    // 경도
    @Column(name = "lng", nullable = false)
    private double lng;

    // 지역명
    @Column(name = "location", nullable = false, length = 30)
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


    @Column(name = "tag", length = 20)
    private String tag;

    @Builder
    public Photo(Long photoId,User user, String photoPath, double lat, double lng,
                 String location, Long registerDatetime, Long uploadDatetime, String tag,
                 boolean frameActive, boolean sharedActive, int likes, int views, String s3FileName) {
        this.photoId = photoId;
        this.userId = user.getUserId();
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
        this.tag = tag;
        this.s3FileName = s3FileName;
    }
}
