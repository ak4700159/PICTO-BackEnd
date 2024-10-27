package picto.com.generator.domain.photo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import picto.com.generator.domain.user.domain.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(schema = "photo_schema", name = "Photo")
public class Photo {
    @Id
    @Column(name = "photo_path", length = 50)
    private String photo_path;

    // 외래키 등록하기
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    User user;

    // 위도
    @Column(name = "lat", nullable = false)
    private double lat;

    // 경도
    @Column(name = "lng", nullable = false)
    private double lng;

    // 지역명
    @Column(name = "location", nullable = false, length = 20)
    private String location;

    @Column(name = "register_time", nullable = false)
    private int registerTime;

    @Column(name = "upload_time", nullable = false)
    private int uploadTime;

    // 액자 활성화 여부
    @ColumnDefault("true")
    @Column(name = "frame_active", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean frame_active;

    // 사진 공유 여부
    @ColumnDefault("false")
    @Column(name = "shared_active", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean shared_active;

    @ColumnDefault("0")
    @Column(name = "likes", nullable = false)
    private int likes;

    @ColumnDefault("0")
    @Column(name = "views", nullable = false)
    private int views;

    @Column(name = "title", length = 20)
    private String title;


    // 좀 더 간단 명료하게 생성자 생성할 수 있게됨
    // @Builder

//    public void update(String title, String content){
//        this.title = title;
//        this.content = content;
//    }
}
