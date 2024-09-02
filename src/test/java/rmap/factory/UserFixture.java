package rmap.factory;

import rmap.entity.User;

public enum UserFixture {
    DEFAULT(-1L, "test@test.com", "testpwd1234"),
    DEFAULT_1(-2L, "test1@test.com", "testpwd1234"),
    DEFAULT_2(-3L, "test2@test.com", "testpwd1234"),
    ;

    private Long id;
    private String email;
    private String password;

    UserFixture(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User user() {
        return UserFactory.builder()
                .email(email)
                .password(password)
                .withId(id)
                .create();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
