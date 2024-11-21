package picto.com.chattingscheduler.domain.session.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import picto.com.chattingscheduler.domain.session.entity.ChattingMsg;
import picto.com.chattingscheduler.domain.session.entity.ChattingMsgId;

import java.util.List;

public interface ChattingMsgRepository extends JpaRepository<ChattingMsg, ChattingMsgId> {
    @Query("select msg from ChattingMsg msg where msg.chattingMsgId.folderId = :folderId")
    List<ChattingMsg> findByfolderId(@Param("folderId") Long folderId);

    @Query("select msg from ChattingMsg msg where msg.chattingMsgId.senderId = :senderId")
    List<ChattingMsg> findBySenderId(@Param("senderId") Long senderId);

    @Query("select msg from ChattingMsg msg where msg.chattingMsgId.senderId = :senderId and msg.chattingMsgId.folderId = :folderId")
    List<ChattingMsg> findBySenderIdAndFolderId(@Param("senderId") Long senderId, @Param("folderId") Long folderId);
}
