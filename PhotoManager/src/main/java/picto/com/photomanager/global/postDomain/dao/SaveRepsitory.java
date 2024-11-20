package picto.com.photomanager.global.postDomain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.photomanager.global.postDomain.entity.Save;
import picto.com.photomanager.global.postDomain.entity.SaveId;

public interface SaveRepsitory extends JpaRepository<Save, SaveId> {
}
