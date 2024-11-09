package picto.com.photomanager.global.postDomain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSaveId is a Querydsl query type for SaveId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QSaveId extends BeanPath<SaveId> {

    private static final long serialVersionUID = 921199847L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSaveId saveId = new QSaveId("saveId");

    public final QSharingFolderId folderId;

    public final picto.com.photomanager.domain.photo.entity.QPhotoId photoId;

    public QSaveId(String variable) {
        this(SaveId.class, forVariable(variable), INITS);
    }

    public QSaveId(Path<? extends SaveId> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSaveId(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSaveId(PathMetadata metadata, PathInits inits) {
        this(SaveId.class, metadata, inits);
    }

    public QSaveId(Class<? extends SaveId> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.folderId = inits.isInitialized("folderId") ? new QSharingFolderId(forProperty("folderId")) : null;
        this.photoId = inits.isInitialized("photoId") ? new picto.com.photomanager.domain.photo.entity.QPhotoId(forProperty("photoId")) : null;
    }

}

