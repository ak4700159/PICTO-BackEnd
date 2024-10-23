package picto.com.generator.domain.photo.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPhotoId is a Querydsl query type for PhotoId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QPhotoId extends BeanPath<PhotoId> {

    private static final long serialVersionUID = -231460520L;

    public static final QPhotoId photoId = new QPhotoId("photoId");

    public final StringPath photoPath = createString("photoPath");

    public final StringPath tag = createString("tag");

    public QPhotoId(String variable) {
        super(PhotoId.class, forVariable(variable));
    }

    public QPhotoId(Path<? extends PhotoId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPhotoId(PathMetadata metadata) {
        super(PhotoId.class, metadata);
    }

}

