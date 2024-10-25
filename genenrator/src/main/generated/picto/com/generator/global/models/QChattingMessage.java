package picto.com.generator.global.models;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChattingMessage is a Querydsl query type for ChattingMessage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChattingMessage extends EntityPathBase<ChattingMessage> {

    private static final long serialVersionUID = -176250441L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChattingMessage chattingMessage = new QChattingMessage("chattingMessage");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> sendTime = createNumber("sendTime", Integer.class);

    public final QSharingFolder sharingFolder;

    public final picto.com.generator.domain.user.domain.QUser user;

    public QChattingMessage(String variable) {
        this(ChattingMessage.class, forVariable(variable), INITS);
    }

    public QChattingMessage(Path<? extends ChattingMessage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChattingMessage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChattingMessage(PathMetadata metadata, PathInits inits) {
        this(ChattingMessage.class, metadata, inits);
    }

    public QChattingMessage(Class<? extends ChattingMessage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sharingFolder = inits.isInitialized("sharingFolder") ? new QSharingFolder(forProperty("sharingFolder"), inits.get("sharingFolder")) : null;
        this.user = inits.isInitialized("user") ? new picto.com.generator.domain.user.domain.QUser(forProperty("user")) : null;
    }

}

