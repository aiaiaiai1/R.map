package rmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rmap.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    default User getByEmail(String email) {
        User user = findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        return user;
    }

//    @Query(value = "select exists (select * from user_account as ua where ua.email = :email)",
//            nativeQuery = true)
//    boolean findByEmail(String email);

    @Query("select u from User as u where u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.nickname = :nickname")
    Optional<User> findByNickname(String nickname);

    @Query("select u from User u where u.id = :id")
    Optional<User> findUserById(Long id);

    default User getByUserId(Long userId) {
        return findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
    }
}
