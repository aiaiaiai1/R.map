package rmap.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class ResetPasswordRequest {

    @NotBlank(message = "email is blank")
    private String email;


    public ResetPasswordRequest(String email) {
        this.email = email;
    }
}
