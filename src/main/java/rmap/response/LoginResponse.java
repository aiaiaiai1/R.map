package rmap.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rmap.entity.User;

@Getter
public class LoginResponse {
    private final Long id;
    private final String nickname;

    public LoginResponse(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
    }
}
