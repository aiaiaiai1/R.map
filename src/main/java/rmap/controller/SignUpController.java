package rmap.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import rmap.entity.UserAccount;
import rmap.request.LoginRequest;
import rmap.request.SendAuthenticationRequest;
import rmap.request.VerificationRequest;
import rmap.service.SignUpService;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/user/email/auth-request")
    public ResponseEntity<Void> sendAuthentication(@Valid @RequestBody SendAuthenticationRequest request) {
        signUpService.sendAuthenticationTo(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/email/auth-check")
    public ResponseEntity<Void> verifyAuthentication(@Valid @RequestBody VerificationRequest request) {
        signUpService.verifyAuthentication(request.getEmail(), request.getCode());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/welcome")
    public ResponseEntity<Void> signUp(@Valid @RequestBody VerificationRequest request) {
        signUpService.signUp(request.getEmail(), request.getCode());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/hello")
    public ResponseEntity<Void> logIn(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        UserAccount userAccount = signUpService.logIn(loginRequest.getEmail(), loginRequest.getPassword());
        HttpSession session = request.getSession();
        session.setAttribute("logined", userAccount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/testing-sesstion")
    public ResponseEntity<String> test(
            @SessionAttribute(name = "logined", required = false) UserAccount userAccount
    ) {
        if (userAccount == null) {
            return ResponseEntity.badRequest().body("로그인 실패");
        }
        return ResponseEntity.ok("로그인 성공");
    }
}
