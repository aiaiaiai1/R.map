package rmap.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rmap.entity.UserAccount;
import rmap.exception.EntityNotFoundException;
import rmap.exception.type.UserAccountExceptionType;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    default UserAccount findByIdOrThrow(Long userAccountId) {
        UserAccount userAccount = findById(userAccountId)
                .orElseThrow(() -> new EntityNotFoundException(UserAccountExceptionType.NOT_FOUND));
        return userAccount;
    }

    @Query("select ua from UserAccount as ua where ua.email = :email")
    Optional<UserAccount> findByEmail(String email);
}
