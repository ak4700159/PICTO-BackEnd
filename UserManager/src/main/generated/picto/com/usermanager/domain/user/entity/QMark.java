package picto.com.usermanager.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMark is a Querydsl query type for Mark
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMark extends EntityPathBase<Mark> {

    private static final long serialVersionUID = 2063186589L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMark mark = new QMark("mark");

    public final QMarkId id;

    public final QUser marked;

    public final QUser marking;

    public QMark(String variable) {
        this(Mark.class, forVariable(variable), INITS);
    }

    public QMark(Path<? extends Mark> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMark(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMark(PathMetadata metadata, PathInits inits) {
        this(Mark.class, metadata, inits);
    }

    public QMark(Class<? extends Mark> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QMarkId(forProperty("id")) : null;
        this.marked = inits.isInitialized("marked") ? new QUser(forProperty("marked")) : null;
        this.marking = inits.isInitialized("marking") ? new QUser(forProperty("marking")) : null;
    }

}

