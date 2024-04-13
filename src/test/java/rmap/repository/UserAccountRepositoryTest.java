package rmap.repository;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserAccountRepositoryTest extends RepositoryTest {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void 이메일로_유저_계정_존재_여부를_확인한다() {
        // given
        String email = "test@test.com";
        String password = "test";
        supporter.유저_계정_저장(email, password);

        // when
        boolean result = userAccountRepository.findByEmail(email);

        // then
        assertThat(result).isTrue();
    }

}
