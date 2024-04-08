package rmap.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerificationRequest {

    @NotBlank(message = "email is blank")
    private String email;

    @NotBlank(message = "code is blank")
    private String code;

    public VerificationRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
