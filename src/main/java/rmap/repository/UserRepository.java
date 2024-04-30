package rmap.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rmap.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    default User findByEmailAndPasswordOrThrow(String email, String password) {
        User user = findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalArgumentException("아이디와 비밀번호를 확인해주세요"));
        return user;
    }

    @Query("select ua from User as ua where ua.email = :email and ua.password = :password")
    Optional<User> findByEmailAndPassword(String email, String password);

//    @Query(value = "select exists (select * from user_account as ua where ua.email = :email)",
//            nativeQuery = true)
//    boolean findByEmail(String email);

    @Query("select ua from User as ua where ua.email = :email")
    Optional<User> findByEmail(String email);
}
