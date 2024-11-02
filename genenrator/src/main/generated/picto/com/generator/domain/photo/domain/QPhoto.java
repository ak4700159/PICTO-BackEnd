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

    public final BooleanPath frame_active = createBoolean("frame_active");

    public final NumberPath<Double> lat = createNumber("lat", Double.class);

    public final NumberPath<Integer> likes = createNumber("likes", Integer.class);

    public final NumberPath<Double> lng = createNumber("lng", Double.class);

    public final StringPath location = createString("location");

    public final NumberPath<Integer> photoID = createNumber("photoID", Integer.class);

    public final StringPath photoPath = createString("photoPath");

    public final NumberPath<Long> registerTime = createNumber("registerTime", Long.class);

    public final BooleanPath shared_active = createBoolean("shared_active");

    public final StringPath title = createString("title");

    public final NumberPath<Long> uploadTime = createNumber("uploadTime", Long.class);

    public final picto.com.generator.domain.user.domain.QUser user;

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
        this.user = inits.isInitialized("user") ? new picto.com.generator.domain.user.domain.QUser(forProperty("user")) : null;
    }

}

