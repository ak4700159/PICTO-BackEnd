package picto.com.photomanager.global.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -157619391L;

    public static final QUser user = new QUser("user");

    public final StringPath accountName = createString("accountName");

    public final StringPath email = createString("email");

    public final StringPath intro = createString("intro");

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final BooleanPath profileActive = createBoolean("profileActive");

    public final StringPath profilePhotoPath = createString("profilePhotoPath");

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

