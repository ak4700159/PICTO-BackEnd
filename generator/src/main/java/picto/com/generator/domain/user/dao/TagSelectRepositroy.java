package picto.com.generator.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.domain.user.entity.TagSelect;
import picto.com.generator.domain.user.entity.TagSelectId;

public interface TagSelectRepositroy extends JpaRepository<TagSelect, TagSelectId> {
}
