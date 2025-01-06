package rmap.response;

import lombok.Getter;
import rmap.entity.User;

@Getter
public class OwnerResponse {
    private Long id;
    private String name;

    public OwnerResponse(User user) {
        this.id = user.getId();
        this.name = user.getNickname();
    }

}
