package rmap.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rmap.entity.User;
import rmap.global.Logined;
import rmap.request.*;
import rmap.response.IsLoginedResponse;
import rmap.response.LoginResponse;
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

    @PatchMapping("/user/email/password")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        signUpService.resetPassword(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/user/password")
    public ResponseEntity<Void> changePassword(
            @Logined User user,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        signUpService.changePassword(user, request.getPassword());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/user/nickname")
    public ResponseEntity<Void> changeNickname(
            @Logined User user,
            @Valid @RequestBody ChangeNicknameRequest request
    ) {
        signUpService.changeNickname(user, request.getNickname());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/bye")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("logined");
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/email/auth-check")
    public ResponseEntity<Void> verifyAuthentication(@Valid @RequestBody VerificationRequest request) {
        signUpService.verifyAuthentication(request.getEmail(), request.getCode());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/welcome")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        signUpService.signUp(request.getEmail(), request.getPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/hello")
    public ResponseEntity<LoginResponse> logIn(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        User user = signUpService.logIn(loginRequest.getEmail(), loginRequest.getPassword());
        HttpSession session = request.getSession();
        session.setAttribute("logined", user);
        return ResponseEntity.ok().body(new LoginResponse(user));
    }

    @GetMapping("/user/auth")
    public ResponseEntity<IsLoginedResponse> isLogined(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.ok().body(IsLoginedResponse.falseResponse());
        }
        User user = (User) session.getAttribute("logined");
        return ResponseEntity.ok().body(IsLoginedResponse.trueResponse(user));
    }

    @DeleteMapping("/user/farewell")
    public ResponseEntity<Void> resign(@Logined User user, HttpServletRequest request) {
        signUpService.resign(user);
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("logined");
        }
        return ResponseEntity.ok().build();
    }

}
