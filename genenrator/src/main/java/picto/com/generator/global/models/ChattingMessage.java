package picto.com.generator.global.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import picto.com.generator.domain.user.domain.User;

@Getter
@Setter
@Entity
@Table(name = "ChattingMessage", schema = "photo_schema")
public class ChattingMessage {
    @Id
    @Column(name = "chatting_id", nullable = false)
    private Integer id;

    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @Column(name = "send_time", nullable = false)
    private Integer sendTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sharing_folder_id", nullable = false, referencedColumnName = "sharing_folder_id")
    private SharingFolder sharingFolder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}