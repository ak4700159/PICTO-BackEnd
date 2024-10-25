package picto.com.generator.global.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "Chatting", schema = "photo_schema")
public class Chatting {
    @Id
    @Column(name = "sharing_folder_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sharing_folder_id", nullable = false, referencedColumnName = "sharing_folder_id")
    private SharingFolder sharingFolder;

    @ColumnDefault("0")
    @Column(name = "active", nullable = false)
    private Byte active;

    @Column(name = "last_send_time", nullable = false)
    private Integer lastSendTime;

}