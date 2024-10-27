package picto.com.generator.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import picto.com.generator.domain.user.domain.User;

import java.util.List;
import java.util.Optional;

//에러 방지를 위해 어노테이션 추가
//beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type .....
// @Repository -> 필요없었음
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.email LIKE %:email%")
    List<User> findEmailName(@Param("email") String email);
}
