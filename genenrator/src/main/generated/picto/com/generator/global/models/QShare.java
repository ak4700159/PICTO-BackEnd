package picto.com.generator.global.models;

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

    private static final long serialVersionUID = -721862091L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShare share = new QShare("share");

    public final QShareId id;

    public final QSharingFolder sharingFolder;

    public final picto.com.generator.domain.user.domain.QUser user;

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
        this.id = inits.isInitialized("id") ? new QShareId(forProperty("id")) : null;
        this.sharingFolder = inits.isInitialized("sharingFolder") ? new QSharingFolder(forProperty("sharingFolder"), inits.get("sharingFolder")) : null;
        this.user = inits.isInitialized("user") ? new picto.com.generator.domain.user.domain.QUser(forProperty("user")) : null;
    }

}

