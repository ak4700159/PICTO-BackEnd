package picto.com.usermanager.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.usermanager.domain.user.entity.TagSelect;
import picto.com.usermanager.domain.user.entity.TagSelectId;

public interface TagSelectRepositroy extends JpaRepository<TagSelect, TagSelectId> {
}
