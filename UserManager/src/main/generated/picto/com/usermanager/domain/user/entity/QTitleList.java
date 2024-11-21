package picto.com.usermanager.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTitleList is a Querydsl query type for TitleList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTitleList extends EntityPathBase<TitleList> {

    private static final long serialVersionUID = -1475311290L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTitleList titleList = new QTitleList("titleList");

    public final QTitle title;

    public final NumberPath<Long> titleListId = createNumber("titleListId", Long.class);

    public final QUser user;

    public QTitleList(String variable) {
        this(TitleList.class, forVariable(variable), INITS);
    }

    public QTitleList(Path<? extends TitleList> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTitleList(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTitleList(PathMetadata metadata, PathInits inits) {
        this(TitleList.class, metadata, inits);
    }

    public QTitleList(Class<? extends TitleList> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.title = inits.isInitialized("title") ? new QTitle(forProperty("title")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

