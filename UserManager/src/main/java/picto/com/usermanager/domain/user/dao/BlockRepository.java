package picto.com.usermanager.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import picto.com.usermanager.domain.user.entity.Block;
import picto.com.usermanager.domain.user.entity.BlockId;

import java.util.List;

public interface BlockRepository extends JpaRepository<Block, BlockId> {
    @Query("select b from Block b where b.blocking.userId = :userId")
    List<Block> findByUserId(@Param("userId") Long userId);
}
