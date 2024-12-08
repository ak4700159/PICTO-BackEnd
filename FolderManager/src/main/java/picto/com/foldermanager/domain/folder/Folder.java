package picto.com.foldermanager.domain.folder;

import picto.com.foldermanager.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generator_id", referencedColumnName = "user_id")
    private User generator;

    // 폴더 이름
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    // 폴더 설명
    @Column(name = "content", nullable = false, length = 50)
    private String content;

    // 폴더 생성 시점
    @Column(name = "created_datetime", nullable = false)
    private Long createdDatetime;

    // 폴더 초대 링크
    @Column(name = "link", nullable = false, length = 30)
    private String link;

    @Builder
    public Folder(User generator, String name, String content) {
        this.generator = generator;
        this.name = name;
        this.content = content;
        this.createdDatetime = System.currentTimeMillis();
        this.link = UUID.randomUUID().toString().substring(0, 30);
    }

    public void update(String name, String content) {
        this.name = name;
        this.content = content;
    }

}