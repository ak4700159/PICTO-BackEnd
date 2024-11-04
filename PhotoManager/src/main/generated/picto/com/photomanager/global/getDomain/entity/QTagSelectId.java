package picto.com.photomanager.global.getDomain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTagSelectId is a Querydsl query type for TagSelectId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QTagSelectId extends BeanPath<TagSelectId> {

    private static final long serialVersionUID = 421530312L;

    public static final QTagSelectId tagSelectId = new QTagSelectId("tagSelectId");

    public final StringPath tag = createString("tag");

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QTagSelectId(String variable) {
        super(TagSelectId.class, forVariable(variable));
    }

    public QTagSelectId(Path<? extends TagSelectId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTagSelectId(PathMetadata metadata) {
        super(TagSelectId.class, metadata);
    }

}

