package rmap.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangeNicknameRequest {

    @NotBlank(message = "nickname is blank")
    private String nickname;


    public ChangeNicknameRequest(String nickname) {
        this.nickname = nickname;
    }
}
