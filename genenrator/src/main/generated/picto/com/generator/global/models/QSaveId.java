package picto.com.generator.global.models;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSaveId is a Querydsl query type for SaveId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QSaveId extends BeanPath<SaveId> {

    private static final long serialVersionUID = -908740638L;

    public static final QSaveId saveId = new QSaveId("saveId");

    public final StringPath photoPath = createString("photoPath");

    public final NumberPath<Integer> sharingFolderId = createNumber("sharingFolderId", Integer.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QSaveId(String variable) {
        super(SaveId.class, forVariable(variable));
    }

    public QSaveId(Path<? extends SaveId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSaveId(PathMetadata metadata) {
        super(SaveId.class, metadata);
    }

}

