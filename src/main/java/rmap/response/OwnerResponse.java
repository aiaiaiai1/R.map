package rmap.response;

import lombok.Getter;
import rmap.entity.User;

@Getter
public class OwnerResponse {
    public static final String 임시_닉네임 = "임시 닉네임";
    private Long id;
    private String name;

    public OwnerResponse(User user) {
        this.id = user.getId();
        this.name = 임시_닉네임;
    }

}
