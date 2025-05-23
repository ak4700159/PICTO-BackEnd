package picto.com.usermanager.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(schema = "picto_schema", name = "UserProfile")
public class UserProfile {
    @EmbeddedId
    private UserProfileId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("photoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "photo_id", nullable = false, referencedColumnName = "photo_id")
    private Photo photo;

    @Builder
    public UserProfile(User user, Photo photo) {
        this.user = user;
        this.photo = photo;
        this.id = new UserProfileId(user.userId, photo.getPhotoId());
    }
}