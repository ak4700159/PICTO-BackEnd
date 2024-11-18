package picto.com.usermanager.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QToken is a Querydsl query type for Token
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QToken extends EntityPathBase<Token> {

    private static final long serialVersionUID = -458850263L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QToken token = new QToken("token");

    public final StringPath accessToken = createString("accessToken");

    public final StringPath refreshToken = createString("refreshToken");

    public final QUser user;

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QToken(String variable) {
        this(Token.class, forVariable(variable), INITS);
    }

    public QToken(Path<? extends Token> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QToken(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QToken(PathMetadata metadata, PathInits inits) {
        this(Token.class, metadata, inits);
    }

    public QToken(Class<? extends Token> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

