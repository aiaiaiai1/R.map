package rmap.response;

import lombok.Getter;

@Getter
public class IsLoginedResponse {

    private final Boolean isLogined;
    private Long userId;

    public IsLoginedResponse(Boolean isLogined, Long userId) {
        this.isLogined = isLogined;
        this.userId = userId;
    }
}
