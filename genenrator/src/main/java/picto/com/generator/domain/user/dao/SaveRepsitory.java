package picto.com.generator.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.domain.user.entity.Save;
import picto.com.generator.domain.user.entity.SaveId;


public interface SaveRepsitory extends JpaRepository<Save, SaveId> {
}
