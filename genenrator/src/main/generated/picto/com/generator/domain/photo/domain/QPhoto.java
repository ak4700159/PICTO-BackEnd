package picto.com.generator.domain.photo.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPhoto is a Querydsl query type for Photo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPhoto extends EntityPathBase<Photo> {

    private static final long serialVersionUID = -429290659L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPhoto photo = new QPhoto("photo");

    public final BooleanPath active = createBoolean("active");

    public final StringPath content = createString("content");

    public final NumberPath<Double> lat = createNumber("lat", Double.class);

    public final NumberPath<Integer> likes = createNumber("likes", Integer.class);

    public final NumberPath<Double> lng = createNumber("lng", Double.class);

    public final StringPath location = createString("location");

    public final QPhotoId photoId;

    public final NumberPath<Integer> registerTime = createNumber("registerTime", Integer.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> uploadTime = createNumber("uploadTime", Integer.class);

    public final picto.com.generator.domain.user.domain.QUser user;

    public final NumberPath<Integer> viewS = createNumber("viewS", Integer.class);

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QPhoto(String variable) {
        this(Photo.class, forVariable(variable), INITS);
    }

    public QPhoto(Path<? extends Photo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPhoto(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPhoto(PathMetadata metadata, PathInits inits) {
        this(Photo.class, metadata, inits);
    }

    public QPhoto(Class<? extends Photo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.photoId = inits.isInitialized("photoId") ? new QPhotoId(forProperty("photoId")) : null;
        this.user = inits.isInitialized("user") ? new picto.com.generator.domain.user.domain.QUser(forProperty("user")) : null;
    }

}
