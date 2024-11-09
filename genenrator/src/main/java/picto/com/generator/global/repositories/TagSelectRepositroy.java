package picto.com.generator.global.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import picto.com.generator.global.entity.TagSelect;
import picto.com.generator.global.entity.TagSelectId;

public interface TagSelectRepositroy extends JpaRepository<TagSelect, TagSelectId> {
}
