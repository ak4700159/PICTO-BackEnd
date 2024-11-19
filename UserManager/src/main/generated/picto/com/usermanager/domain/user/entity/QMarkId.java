package picto.com.usermanager.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMarkId is a Querydsl query type for MarkId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QMarkId extends BeanPath<MarkId> {

    private static final long serialVersionUID = -1552576360L;

    public static final QMarkId markId = new QMarkId("markId");

    public final NumberPath<Integer> markedId = createNumber("markedId", Integer.class);

    public final NumberPath<Integer> markingId = createNumber("markingId", Integer.class);

    public QMarkId(String variable) {
        super(MarkId.class, forVariable(variable));
    }

    public QMarkId(Path<? extends MarkId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMarkId(PathMetadata metadata) {
        super(MarkId.class, metadata);
    }

}

