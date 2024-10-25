package picto.com.generator.global.models;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserSetting is a Querydsl query type for UserSetting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserSetting extends EntityPathBase<UserSetting> {

    private static final long serialVersionUID = -1175500325L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserSetting userSetting = new QUserSetting("userSetting");

    public final NumberPath<Byte> arroundAlert = createNumber("arroundAlert", Byte.class);

    public final NumberPath<Byte> autoRotation = createNumber("autoRotation", Byte.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Byte> lightMode = createNumber("lightMode", Byte.class);

    public final NumberPath<Byte> popluarAlert = createNumber("popluarAlert", Byte.class);

    public final picto.com.generator.domain.user.domain.QUser user;

    public QUserSetting(String variable) {
        this(UserSetting.class, forVariable(variable), INITS);
    }

    public QUserSetting(Path<? extends UserSetting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserSetting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserSetting(PathMetadata metadata, PathInits inits) {
        this(UserSetting.class, metadata, inits);
    }

    public QUserSetting(Class<? extends UserSetting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new picto.com.generator.domain.user.domain.QUser(forProperty("user")) : null;
    }

}

