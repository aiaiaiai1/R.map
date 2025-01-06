package rmap.response;

import lombok.Getter;
import rmap.entity.User;

@Getter
public class IsLoginedResponse {

    private final Boolean isLogined;
    private Long userId;
    private String userName;

    public IsLoginedResponse(Boolean isLogined, Long userId, String userName) {
        this.isLogined = isLogined;
        this.userId = userId;
        this.userName = userName;
    }

    public static IsLoginedResponse falseResponse() {
        return new IsLoginedResponse(false, null, null);
    }

    public static IsLoginedResponse trueResponse(User user) {
        return new IsLoginedResponse(true, user.getId(), user.getNickname());
    }
}
