package picto.com.photomanager.global.getDomain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFilter is a Querydsl query type for Filter
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFilter extends EntityPathBase<Filter> {

    private static final long serialVersionUID = -286879935L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFilter filter = new QFilter("filter");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath period = createString("period");

    public final StringPath sort = createString("sort");

    public final NumberPath<Long> startTime = createNumber("startTime", Long.class);

    public final picto.com.photomanager.global.user.entity.QUser user;

    public QFilter(String variable) {
        this(Filter.class, forVariable(variable), INITS);
    }

    public QFilter(Path<? extends Filter> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFilter(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFilter(PathMetadata metadata, PathInits inits) {
        this(Filter.class, metadata, inits);
    }

    public QFilter(Class<? extends Filter> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new picto.com.photomanager.global.user.entity.QUser(forProperty("user")) : null;
    }

}
