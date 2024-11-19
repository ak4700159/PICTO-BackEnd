package picto.com.usermanager.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBlockId is a Querydsl query type for BlockId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QBlockId extends BeanPath<BlockId> {

    private static final long serialVersionUID = -1745889096L;

    public static final QBlockId blockId = new QBlockId("blockId");

    public final NumberPath<Integer> blockedId = createNumber("blockedId", Integer.class);

    public final NumberPath<Integer> blockingId = createNumber("blockingId", Integer.class);

    public QBlockId(String variable) {
        super(BlockId.class, forVariable(variable));
    }

    public QBlockId(Path<? extends BlockId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBlockId(PathMetadata metadata) {
        super(BlockId.class, metadata);
    }

}

