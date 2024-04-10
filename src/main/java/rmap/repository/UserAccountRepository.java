package rmap.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rmap.entity.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    default UserAccount findByEmailAndPasswordOrThrow(String email, String password) {
        UserAccount userAccount = findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalArgumentException("아이디와 비밀번호를 확인해주세요"));
        return userAccount;
    }

    @Query("select ua from UserAccount as ua where ua.email = :email and ua.password = :password")
    Optional<UserAccount> findByEmailAndPassword(String email, String password);

    @Query(value = "select exists (select * from user_account as ua where ua.email = :email)",
            nativeQuery = true)
    boolean findByEmail(String email);
}
