package rmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmap.entity.UserAccount;
import rmap.exception.EntityNotFoundException;
import rmap.exception.type.UserAccountExceptionType;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    default UserAccount findByIdOrThrow(Long userAccountId) {
        UserAccount userAccount = findById(userAccountId)
                .orElseThrow(() -> new EntityNotFoundException(UserAccountExceptionType.NOT_FOUND));
        return userAccount;
    }
}
