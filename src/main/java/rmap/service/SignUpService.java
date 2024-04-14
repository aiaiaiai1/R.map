package rmap.service;

import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmap.entity.User;
import rmap.entity.UserAccount;
import rmap.repository.UserAccountRepository;
import rmap.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final JavaMailSender mailSender;
    private final Cache emailAuthenticationCodeCache = new Cache(3 * 60 * 1000);
    private final Cache verifiedEmailCash = new Cache(1 * 60 * 1000);
    private final UserAccountRepository userAccountRepository;
    private final UserRepository userRepository;


    public void sendAuthenticationTo(String email) {
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        if (!Pattern.matches(regex, email)) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
        validateAlreadyRegistered(email);
        String code = CodeGenerator.generateRandomCode();
        sendMessage(email, code);
        emailAuthenticationCodeCache.put(email, code);
    }


    private void sendMessage(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("rmap 회원가입 인증 코드");
        message.setText("[Rmap] 인증코드: " + code
                + System.lineSeparator() +
                "이 인증코드는 3분간 유효합니다.");
        message.setTo(email);
        mailSender.send(message);
    }

    public void verifyAuthentication(String email, String code) {
        if (!emailAuthenticationCodeCache.containsKey(email)) {
            throw new IllegalArgumentException("인증코드를 발송해 주세요");
        }
        if (!emailAuthenticationCodeCache.containsKeyValue(email, code)) {
            throw new IllegalArgumentException("인증코드가 일치하지 않습니다.");
        }
        emailAuthenticationCodeCache.clearKey(email);
        verifiedEmailCash.put(email, email);
    }

    @Transactional
    public void signUp(String email, String password) {
        validateAlreadyRegistered(email);
        if (!verifiedEmailCash.containsKey(email)) {
            throw new IllegalArgumentException("인증되지 않았습니다");
        }

        User user = new User();
        User savedUser = userRepository.save(user);
        UserAccount userAccount = new UserAccount(savedUser, email, password);
        userAccountRepository.save(userAccount);
    }

    private void validateAlreadyRegistered(String email) {
        if (userAccountRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일 입니다.");
        }
    }

    public UserAccount logIn(String email, String password) {
        return userAccountRepository.findByEmailAndPasswordOrThrow(email, password);
    }


}
