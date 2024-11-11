package picto.com.sessionscheduler.domain.session.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import picto.com.sessionscheduler.domain.session.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.email LIKE %:email%")
    List<User> findEmailName(@Param("email") String email);
}
