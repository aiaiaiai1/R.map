package rmap.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendAuthenticationRequest {

    @NotBlank(message = "email is blank")
    private String email;

    public SendAuthenticationRequest(String email) {
        this.email = email;
    }
}
