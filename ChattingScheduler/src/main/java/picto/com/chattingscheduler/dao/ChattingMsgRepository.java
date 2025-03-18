package picto.com.chattingscheduler.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import picto.com.chattingscheduler.entity.ChattingMsg;
import java.util.List;

public interface ChattingMsgRepository extends JpaRepository<ChattingMsg, Long> {
    @Query("select msg from ChattingMsg msg where msg.folderId = :folderId")
    List<ChattingMsg> findByfolderId(@Param("folderId") Long folderId);

    @Query("select msg from ChattingMsg msg where msg.senderId = :senderId")
    List<ChattingMsg> findBySenderId(@Param("senderId") Long senderId);

    @Query("select msg from ChattingMsg msg where msg.folderId = :folderId and msg.senderId = :senderId")
    List<ChattingMsg> findByFolderIdAndSenderId(@Param("folderId") Long folderId, @Param("senderId") Long senderId);

    @Query("select msg from ChattingMsg msg where msg.folderId = :folderId and msg.sendDatetime between :start and :end")
    List<ChattingMsg> findByFolderIdAndSendDatetimeBetween(@Param("folderId") Long folderId,
            @Param("start") long start, @Param("end") long end);
}
