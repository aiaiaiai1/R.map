package rmap.response;

import lombok.Getter;

@Getter
public class IsLoginedResponse {

    private final Boolean isLogined;

    public IsLoginedResponse(Boolean isLogined) {
        this.isLogined = isLogined;
    }
}
