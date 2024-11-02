package picto.com.generator.global.models;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPhotoRecord is a Querydsl query type for PhotoRecord
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPhotoRecord extends EntityPathBase<PhotoRecord> {

    private static final long serialVersionUID = -1801059591L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPhotoRecord photoRecord = new QPhotoRecord("photoRecord");

    public final picto.com.generator.domain.user.domain.QUser agent;

    public final NumberPath<Long> eventTime = createNumber("eventTime", Long.class);

    public final QPhotoRecordId id;

    public final picto.com.generator.domain.user.domain.QUser owner;

    public final picto.com.generator.domain.photo.domain.QPhoto photoId;

    public final NumberPath<Byte> type = createNumber("type", Byte.class);

    public QPhotoRecord(String variable) {
        this(PhotoRecord.class, forVariable(variable), INITS);
    }

    public QPhotoRecord(Path<? extends PhotoRecord> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPhotoRecord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPhotoRecord(PathMetadata metadata, PathInits inits) {
        this(PhotoRecord.class, metadata, inits);
    }

    public QPhotoRecord(Class<? extends PhotoRecord> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.agent = inits.isInitialized("agent") ? new picto.com.generator.domain.user.domain.QUser(forProperty("agent")) : null;
        this.id = inits.isInitialized("id") ? new QPhotoRecordId(forProperty("id")) : null;
        this.owner = inits.isInitialized("owner") ? new picto.com.generator.domain.user.domain.QUser(forProperty("owner")) : null;
        this.photoId = inits.isInitialized("photoId") ? new picto.com.generator.domain.photo.domain.QPhoto(forProperty("photoId"), inits.get("photoId")) : null;
    }

}

