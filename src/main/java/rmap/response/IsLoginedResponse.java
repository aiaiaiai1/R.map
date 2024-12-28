package rmap.response;

import lombok.Getter;
import rmap.entity.User;

@Getter
public class IsLoginedResponse {

    private final Boolean isLogined;
    private Long userId;
    private String userName;

    public IsLoginedResponse(Boolean isLogined, Long userId) {
        this.isLogined = isLogined;
        this.userId = userId;
        this.userName = "임시 닉네임";
    }
}
