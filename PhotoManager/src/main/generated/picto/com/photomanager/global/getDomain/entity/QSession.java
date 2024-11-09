package picto.com.photomanager.global.getDomain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSession is a Querydsl query type for Session
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSession extends EntityPathBase<Session> {

    private static final long serialVersionUID = -1758775315L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSession session = new QSession("session");

    public final BooleanPath active = createBoolean("active");

    public final NumberPath<Double> currentLat = createNumber("currentLat", Double.class);

    public final NumberPath<Double> currentLng = createNumber("currentLng", Double.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath location = createString("location");

    public final picto.com.photomanager.global.user.entity.QUser user;

    public QSession(String variable) {
        this(Session.class, forVariable(variable), INITS);
    }

    public QSession(Path<? extends Session> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSession(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSession(PathMetadata metadata, PathInits inits) {
        this(Session.class, metadata, inits);
    }

    public QSession(Class<? extends Session> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new picto.com.photomanager.global.user.entity.QUser(forProperty("user")) : null;
    }

}

