package picto.com.generator.global.models;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTitle is a Querydsl query type for Title
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTitle extends EntityPathBase<Title> {

    private static final long serialVersionUID = -720890706L;

    public static final QTitle title = new QTitle("title");

    public final StringPath condition = createString("condition");

    public final StringPath content = createString("content");

    public final StringPath name = createString("name");

    public final SetPath<picto.com.generator.domain.photo.domain.Photo, picto.com.generator.domain.photo.domain.QPhoto> photos = this.<picto.com.generator.domain.photo.domain.Photo, picto.com.generator.domain.photo.domain.QPhoto>createSet("photos", picto.com.generator.domain.photo.domain.Photo.class, picto.com.generator.domain.photo.domain.QPhoto.class, PathInits.DIRECT2);

    public QTitle(String variable) {
        super(Title.class, forVariable(variable));
    }

    public QTitle(Path<? extends Title> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTitle(PathMetadata metadata) {
        super(Title.class, metadata);
    }

}

