package picto.com.photomanager.global.postDomain.entity;

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

    private static final long serialVersionUID = 710915099L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSharingFolder sharingFolder = new QSharingFolder("sharingFolder");

    public final StringPath content = createString("content");

    public final QSharingFolderId id;

    public final StringPath name = createString("name");

    public final NumberPath<Long> savedDatetime = createNumber("savedDatetime", Long.class);

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
        this.id = inits.isInitialized("id") ? new QSharingFolderId(forProperty("id")) : null;
    }

}

