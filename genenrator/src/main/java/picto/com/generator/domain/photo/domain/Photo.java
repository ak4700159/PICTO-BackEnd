package picto.com.generator.domain.photo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import picto.com.generator.domain.user.domain.User;

// 여기서 사용자를 외래키롤 지정

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(schema = "photo_schema", name = "Photo")
public class Photo {
    // 태그를 외래키에서 PK로 포함시켜 복합키를 만들었다.
    // PhotoId 클래스를 만들어 Serializable 인터페이스를 구현한다.
    // 그리고나서 여기에 사용하면 된다.
//    @EmbeddedId
//    private PhotoId photoId;
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
    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "register_time", nullable = false)
    private int registerTime;

    @Column(name = "upload_time", nullable = false)
    private int uploadTime;

    // 액자 활성화 여부
    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "likes", nullable = false)
    private int likes;

    @Column(name = "views", nullable = false)
    private int views;

    @Column(name = "title", length = 20)
    private String title;

    @ColumnDefault("0")
    @Column(name = "`views`", nullable = false)
    private Integer viewS;

    public void setViewS(Integer viewS) {
        this.viewS = viewS;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // 좀 더 간단 명료하게 생성자 생성할 수 있게됨
    // @Builder

//    public void update(String title, String content){
//        this.title = title;
//        this.content = content;
//    }
}
