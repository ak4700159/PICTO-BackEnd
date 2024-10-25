package picto.com.generator.global.models;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QShareId is a Querydsl query type for ShareId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QShareId extends BeanPath<ShareId> {

    private static final long serialVersionUID = 2075234864L;

    public static final QShareId shareId = new QShareId("shareId");

    public final StringPath link = createString("link");

    public final NumberPath<Integer> sharingFolderId = createNumber("sharingFolderId", Integer.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QShareId(String variable) {
        super(ShareId.class, forVariable(variable));
    }

    public QShareId(Path<? extends ShareId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QShareId(PathMetadata metadata) {
        super(ShareId.class, metadata);
    }

}

