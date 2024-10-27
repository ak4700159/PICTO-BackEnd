package picto.com.generator.global.models;

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

    private static final long serialVersionUID = 882258540L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTitleList titleList = new QTitleList("titleList");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QTitle name;

    public final picto.com.generator.domain.photo.domain.QPhoto user;

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
        this.name = inits.isInitialized("name") ? new QTitle(forProperty("name")) : null;
        this.user = inits.isInitialized("user") ? new picto.com.generator.domain.photo.domain.QPhoto(forProperty("user"), inits.get("user")) : null;
    }

}

