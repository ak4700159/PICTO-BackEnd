package picto.com.generator.global.entity;

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

    private static final long serialVersionUID = -890820548L;

    public static final QPhotoId photoId1 = new QPhotoId("photoId1");

    public final NumberPath<Long> photoId = createNumber("photoId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

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

