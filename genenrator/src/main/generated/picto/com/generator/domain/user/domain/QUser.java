package picto.com.generator.domain.user.domain;

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

    private static final long serialVersionUID = -1206365141L;

    public static final QUser user = new QUser("user");

    public final StringPath account_name = createString("account_name");

    public final StringPath email = createString("email");

    public final StringPath intro = createString("intro");

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final NumberPath<Integer> profile_active = createNumber("profile_active", Integer.class);

    public final StringPath profile_photo_path = createString("profile_photo_path");

    public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);

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

