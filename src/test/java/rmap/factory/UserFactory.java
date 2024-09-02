package rmap.factory;

import org.springframework.test.util.ReflectionTestUtils;
import rmap.entity.User;

import java.time.LocalDateTime;

public class UserFactory {

    private UserFactory() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String email;
        private String password;
        private LocalDateTime createdAt;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public User create() {
            User user = new User(email, password, createdAt);
            ReflectionTestUtils.setField(user, "id", id);
            return user;
        }

    }

}
