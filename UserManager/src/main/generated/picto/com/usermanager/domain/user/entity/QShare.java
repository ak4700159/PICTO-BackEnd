package picto.com.usermanager.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShare is a Querydsl query type for Share
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShare extends EntityPathBase<Share> {

    private static final long serialVersionUID = -459991537L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShare share = new QShare("share");

    public final QFolder folder;

    public final QShareId id;

    public final NumberPath<Long> sharedDatetime = createNumber("sharedDatetime", Long.class);

    public final QUser user;

    public QShare(String variable) {
        this(Share.class, forVariable(variable), INITS);
    }

    public QShare(Path<? extends Share> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QShare(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QShare(PathMetadata metadata, PathInits inits) {
        this(Share.class, metadata, inits);
    }

    public QShare(Class<? extends Share> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.folder = inits.isInitialized("folder") ? new QFolder(forProperty("folder"), inits.get("folder")) : null;
        this.id = inits.isInitialized("id") ? new QShareId(forProperty("id")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

