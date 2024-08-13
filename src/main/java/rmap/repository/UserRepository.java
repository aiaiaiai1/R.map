package rmap.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rmap.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    default User getByEmail(String email) {
        User user = findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        return user;
    }

//    @Query(value = "select exists (select * from user_account as ua where ua.email = :email)",
//            nativeQuery = true)
//    boolean findByEmail(String email);

    @Query("select ua from User as ua where ua.email = :email")
    Optional<User> findByEmail(String email);
}
