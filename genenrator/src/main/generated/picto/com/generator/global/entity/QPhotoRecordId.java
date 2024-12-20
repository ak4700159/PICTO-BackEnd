package picto.com.generator.global.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPhotoRecordId is a Querydsl query type for PhotoRecordId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QPhotoRecordId extends BeanPath<PhotoRecordId> {

    private static final long serialVersionUID = -1410260051L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPhotoRecordId photoRecordId = new QPhotoRecordId("photoRecordId");

    public final NumberPath<Long> agentId = createNumber("agentId", Long.class);

    public final QPhotoId photoId;

    public QPhotoRecordId(String variable) {
        this(PhotoRecordId.class, forVariable(variable), INITS);
    }

    public QPhotoRecordId(Path<? extends PhotoRecordId> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPhotoRecordId(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPhotoRecordId(PathMetadata metadata, PathInits inits) {
        this(PhotoRecordId.class, metadata, inits);
    }

    public QPhotoRecordId(Class<? extends PhotoRecordId> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.photoId = inits.isInitialized("photoId") ? new QPhotoId(forProperty("photoId")) : null;
    }

}

