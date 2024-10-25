package picto.com.generator.global.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import picto.com.generator.domain.user.domain.User;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "SharingFolder", schema = "photo_schema")
public class SharingFolder {
    @EmbeddedId
    private SharingFolderId id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "generated_time", nullable = false)
    private Integer generatedTime;

    @Column(name = "content", nullable = false, length = 50)
    private String content;

    @OneToOne(mappedBy = "sharingFolder")
    private Chatting chatting;

    @OneToMany(mappedBy = "sharingFolder")
    private Set<ChattingMessage> chattingMessages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sharingFolder")
    private Set<Save> saves = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "sharingFolder")
    private Set<User> users = new LinkedHashSet<>();

}