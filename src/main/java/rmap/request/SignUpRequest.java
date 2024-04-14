package rmap.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "email is blank")
    private String email;

    @NotBlank(message = "password is blank")
    private String password;

    public SignUpRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
