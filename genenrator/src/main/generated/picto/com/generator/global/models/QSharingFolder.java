package picto.com.generator.global.models;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSharingFolder is a Querydsl query type for SharingFolder
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSharingFolder extends EntityPathBase<SharingFolder> {

    private static final long serialVersionUID = -587652928L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSharingFolder sharingFolder = new QSharingFolder("sharingFolder");

    public final QChatting chatting;

    public final SetPath<ChattingMessage, QChattingMessage> chattingMessages = this.<ChattingMessage, QChattingMessage>createSet("chattingMessages", ChattingMessage.class, QChattingMessage.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final NumberPath<Integer> generatedTime = createNumber("generatedTime", Integer.class);

    public final QSharingFolderId id;

    public final StringPath name = createString("name");

    public final SetPath<Save, QSave> saves = this.<Save, QSave>createSet("saves", Save.class, QSave.class, PathInits.DIRECT2);

    public final SetPath<picto.com.generator.domain.user.domain.User, picto.com.generator.domain.user.domain.QUser> users = this.<picto.com.generator.domain.user.domain.User, picto.com.generator.domain.user.domain.QUser>createSet("users", picto.com.generator.domain.user.domain.User.class, picto.com.generator.domain.user.domain.QUser.class, PathInits.DIRECT2);

    public QSharingFolder(String variable) {
        this(SharingFolder.class, forVariable(variable), INITS);
    }

    public QSharingFolder(Path<? extends SharingFolder> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSharingFolder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSharingFolder(PathMetadata metadata, PathInits inits) {
        this(SharingFolder.class, metadata, inits);
    }

    public QSharingFolder(Class<? extends SharingFolder> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatting = inits.isInitialized("chatting") ? new QChatting(forProperty("chatting"), inits.get("chatting")) : null;
        this.id = inits.isInitialized("id") ? new QSharingFolderId(forProperty("id")) : null;
    }

}

