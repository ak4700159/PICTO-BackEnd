package picto.com.photomanager.global.postDomain.entity;

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

    private static final long serialVersionUID = -1044401900L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPhotoRecord photoRecord = new QPhotoRecord("photoRecord");

    public final picto.com.photomanager.global.user.entity.QUser agent;

    public final NumberPath<Long> eventDatetime = createNumber("eventDatetime", Long.class);

    public final QPhotoRecordId id;

    public final picto.com.photomanager.domain.photo.entity.QPhoto photo;

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
        this.agent = inits.isInitialized("agent") ? new picto.com.photomanager.global.user.entity.QUser(forProperty("agent")) : null;
        this.id = inits.isInitialized("id") ? new QPhotoRecordId(forProperty("id"), inits.get("id")) : null;
        this.photo = inits.isInitialized("photo") ? new picto.com.photomanager.domain.photo.entity.QPhoto(forProperty("photo"), inits.get("photo")) : null;
    }

}

