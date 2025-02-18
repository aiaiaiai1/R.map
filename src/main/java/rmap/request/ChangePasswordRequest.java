package rmap.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangePasswordRequest {

    @NotBlank(message = "password is blank")
    private String password;


    public ChangePasswordRequest(String password) {
        this.password = password;
    }
}
