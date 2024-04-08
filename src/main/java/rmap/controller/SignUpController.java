package rmap.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import rmap.request.SendAuthenticationRequest;
import rmap.request.VerificationRequest;
import rmap.service.SignUpService;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/user/email/auth-request")
    public ResponseEntity<Void> sendAuthentication(@Valid SendAuthenticationRequest request) {
        signUpService.sendAuthenticationTo(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/email/auth-check")
    public ResponseEntity<Void> verifyAuthentication(@Valid VerificationRequest request) {
        signUpService.verifyAuthentication(request.getEmail(), request.getCode());
        return ResponseEntity.ok().build();
    }
}
