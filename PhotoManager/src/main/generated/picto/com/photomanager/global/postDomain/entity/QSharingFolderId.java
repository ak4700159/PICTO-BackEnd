package picto.com.photomanager.global.postDomain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSharingFolderId is a Querydsl query type for SharingFolderId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QSharingFolderId extends BeanPath<SharingFolderId> {

    private static final long serialVersionUID = 289612438L;

    public static final QSharingFolderId sharingFolderId1 = new QSharingFolderId("sharingFolderId1");

    public final StringPath link = createString("link");

    public final NumberPath<Integer> sharingFolderId = createNumber("sharingFolderId", Integer.class);

    public QSharingFolderId(String variable) {
        super(SharingFolderId.class, forVariable(variable));
    }

    public QSharingFolderId(Path<? extends SharingFolderId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSharingFolderId(PathMetadata metadata) {
        super(SharingFolderId.class, metadata);
    }

}

