package picto.com.photomanager.global.getDomain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTagSelect is a Querydsl query type for TagSelect
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTagSelect extends EntityPathBase<TagSelect> {

    private static final long serialVersionUID = -1134755635L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTagSelect tagSelect = new QTagSelect("tagSelect");

    public final QTagSelectId id;

    public final picto.com.photomanager.global.user.entity.QUser user;

    public QTagSelect(String variable) {
        this(TagSelect.class, forVariable(variable), INITS);
    }

    public QTagSelect(Path<? extends TagSelect> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTagSelect(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTagSelect(PathMetadata metadata, PathInits inits) {
        this(TagSelect.class, metadata, inits);
    }

    public QTagSelect(Class<? extends TagSelect> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QTagSelectId(forProperty("id")) : null;
        this.user = inits.isInitialized("user") ? new picto.com.photomanager.global.user.entity.QUser(forProperty("user")) : null;
    }

}

