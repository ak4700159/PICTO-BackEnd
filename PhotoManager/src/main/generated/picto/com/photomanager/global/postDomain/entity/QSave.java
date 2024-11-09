package picto.com.photomanager.global.postDomain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSave is a Querydsl query type for Save
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSave extends EntityPathBase<Save> {

    private static final long serialVersionUID = 1113806508L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSave save = new QSave("save");

    public final QSaveId id;

    public final picto.com.photomanager.domain.photo.entity.QPhoto photo;

    public final NumberPath<Long> savedDatetime = createNumber("savedDatetime", Long.class);

    public final QSharingFolder sharingFolder;

    public QSave(String variable) {
        this(Save.class, forVariable(variable), INITS);
    }

    public QSave(Path<? extends Save> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSave(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSave(PathMetadata metadata, PathInits inits) {
        this(Save.class, metadata, inits);
    }

    public QSave(Class<? extends Save> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QSaveId(forProperty("id"), inits.get("id")) : null;
        this.photo = inits.isInitialized("photo") ? new picto.com.photomanager.domain.photo.entity.QPhoto(forProperty("photo"), inits.get("photo")) : null;
        this.sharingFolder = inits.isInitialized("sharingFolder") ? new QSharingFolder(forProperty("sharingFolder"), inits.get("sharingFolder")) : null;
    }

}

