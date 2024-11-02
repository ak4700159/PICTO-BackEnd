package picto.com.generator.global.models;

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

    private static final long serialVersionUID = 53555700L;

    public static final QPhotoRecordId photoRecordId = new QPhotoRecordId("photoRecordId");

    public final NumberPath<Integer> agentId = createNumber("agentId", Integer.class);

    public final NumberPath<Integer> ownerId = createNumber("ownerId", Integer.class);

    public final NumberPath<Integer> photoId = createNumber("photoId", Integer.class);

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

