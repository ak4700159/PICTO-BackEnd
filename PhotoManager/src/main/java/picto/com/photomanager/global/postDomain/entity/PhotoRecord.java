package picto.com.photomanager.global.postDomain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.domain.user.entity.User;

@Getter
@Setter
@Entity
@Table(name = "PhotoRecord", indexes = {
        @Index(name = "owner_id", columnList = "owner_id"),
        @Index(name = "photo_id", columnList = "photo_id")
})
public class PhotoRecord {
    // 복합키
    @EmbeddedId
    private PhotoRecordId id;

    // 사용되는 외래키가 기본키로 사용되는 경우 이를 매핑해주는 역할
    @MapsId("agentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    // 테이블 컬럼 명시
    @JoinColumn(name = "agent_id", nullable = false, referencedColumnName = "user_id")
    private User agent;

    // 복합키를 포함해서 새로운 복합키를 만들때 발생하는 오류
    // ~ must have a '@JoinColumn' which references the foreign key column ~
    // @JoinColumns 사용
    @MapsId("photoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumns({
            @JoinColumn(name = "photo_id", nullable = false, referencedColumnName = "photo_id"),
            @JoinColumn(name = "owner_id", nullable = false, referencedColumnName = "user_id")
    })
    private Photo photo;

    @Column(name = "type", nullable = false)
    private Byte type;

    @Column(name = "event_datetime", nullable = false)
    private Long eventDatetime;
}