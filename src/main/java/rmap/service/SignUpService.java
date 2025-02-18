package rmap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmap.entity.User;
import rmap.repository.UserRepository;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final JavaMailSender mailSender;
    private final Cache emailAuthenticationCodeCache = new Cache(3 * 60 * 1000);
    private final Cache verifiedEmailCash = new Cache(1 * 60 * 1000);
    private final UserRepository userAccountRepository;


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
        message.setSubject("[Rmap] 인증 코드");
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
        User user = new User(email, password);
        String nickname;
        do {
            nickname = RandomNicknameGenerator.generate();
        } while (isAlreadyUsed(nickname));
        user.setNickname(nickname);
        userAccountRepository.save(user);
    }

    @Transactional
    public void resetPassword(String email) {
        validateAlreadyRegistered(email);
        if (!verifiedEmailCash.containsKey(email)) {
            throw new IllegalArgumentException("인증되지 않았습니다");
        }
        User user = userAccountRepository.getByEmail(email);
        String password = RandomPasswordGenerator.generate();
        user.changePassword(password);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("[Rmap] 비밀번호 초기화");
        message.setText("[Rmap] 새 비밀번호: " + password);
        message.setTo(email);
        mailSender.send(message);
    }

    private void validateAlreadyRegistered(String email) {
        if (userAccountRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일 입니다.");
        }
    }

    private boolean isAlreadyUsed(String nickname) {
        return userAccountRepository.findByNickname(nickname).isPresent();
    }

    public User logIn(String email, String plainPassword) {
        User user = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일 입니다."));
        if (!user.matchesPassword(plainPassword)) {
            throw new IllegalArgumentException("비밀번호를 확인해 주세요.");
        }
        return user;
    }


    @Transactional
    public void resign(User loginedUser) {
        loginedUser.resign();
    }

    @Transactional
    public void changePassword(User loginedUser, String password) {
        loginedUser.changePassword(password);
    }
}
