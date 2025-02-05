package picto.com.generator.global.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPhotoRecordId is a Querydsl query type for PhotoRecordId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QPhotoRecordId extends BeanPath<PhotoRecordId> {

    private static final long serialVersionUID = -1410260051L;

    public static final QPhotoRecordId photoRecordId = new QPhotoRecordId("photoRecordId");

    public final NumberPath<Long> agentId = createNumber("agentId", Long.class);

    public final NumberPath<Long> photoId = createNumber("photoId", Long.class);

    public QPhotoRecordId(String variable) {
        super(PhotoRecordId.class, forVariable(variable));
    }

    public QPhotoRecordId(Path<? extends PhotoRecordId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPhotoRecordId(PathMetadata metadata) {
        super(PhotoRecordId.class, metadata);
    }

}

