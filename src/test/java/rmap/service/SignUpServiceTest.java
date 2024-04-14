package rmap.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static rmap.EntityCreationSupporter.유저_계정_생성;
import static rmap.EntityCreationSupporter.유저_생성;

import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import rmap.entity.User;
import rmap.entity.UserAccount;
import rmap.repository.UserAccountRepository;
import rmap.repository.UserRepository;

class SignUpServiceTest extends ServiceTest {

    @InjectMocks
    SignUpService signUpService;

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    UserAccountRepository userAccountRepository;

    @Mock
    UserRepository userRepository;


    @Nested
    class 인증_코드_보내기 {

        @Test
        void 이메일_형식이_올바르지_않으면_예외가_발생한다() {
            // given
            String email = "test.com";

            // when, then
            assertThatThrownBy(() -> signUpService.sendAuthenticationTo(email))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이메일 형식이 올바르지 않습니다.");
        }

        @Test
        void 이미_가입된_이메일이면_예외가_발생한다() {
            // given
            String email = "test@test.com";
            String password = "password";
            User user = 유저_생성(1L);
            UserAccount userAccount = 유저_계정_생성(1L, user, email, password);
            given(userAccountRepository.findByEmail(email)).willReturn(Optional.of(userAccount));

            // when, then
            assertThatThrownBy(() -> signUpService.sendAuthenticationTo(email))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 가입된 이메일 입니다.");
        }

        @Nested
        class 이메일_인증 {
            @Test
            void 캐시_키_값에_이메일이_없으면_예외가_발생한다() {
                // given
                String email = "test@test.com";

                willDoNothing().given(javaMailSender).send(any(SimpleMailMessage.class));
                signUpService.sendAuthenticationTo(email);

                Cache emailAuthenticationCodeCache = (Cache) ReflectionTestUtils.getField(signUpService,
                        "emailAuthenticationCodeCache");
                Map<String, String> cache = (Map) ReflectionTestUtils.getField(emailAuthenticationCodeCache, "cache");
                String code = cache.get(email);

                // when, then
                assertThatThrownBy(() -> signUpService.verifyAuthentication("123@123.com", code))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("인증코드를 발송해 주세요");
            }

            @Test
            void 이메일에_전송된_인증코드와_다르면_예외가_발생한다() {
                // given
                String email = "test@test.com";

                willDoNothing().given(javaMailSender).send(any(SimpleMailMessage.class));
                signUpService.sendAuthenticationTo(email);

                // when, then
                assertThatThrownBy(() -> signUpService.verifyAuthentication(email, ""))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("인증코드가 일치하지 않습니다.");
            }

            @Test
            void 이메일_인증이_성공하면_캐시_키를_삭제한다() {
                // given
                String email = "test@test.com";

                willDoNothing().given(javaMailSender).send(any(SimpleMailMessage.class));
                signUpService.sendAuthenticationTo(email);

                Cache emailAuthenticationCodeCache = (Cache) ReflectionTestUtils.getField(signUpService,
                        "emailAuthenticationCodeCache");
                Map<String, String> cache = (Map) ReflectionTestUtils.getField(emailAuthenticationCodeCache, "cache");
                String code = cache.get(email);

                // when
                signUpService.verifyAuthentication(email, code);

                // then
                assertThat(cache).hasSize(0);
            }
        }

        @Nested
        class 회원_가입 {

            @Test
            void 이메일_인증이_안된_경우_예외가_발생한다() {
                // given
                String email = "test@test.com";

                // when, then
                assertThatThrownBy(() -> signUpService.signUp(email, "password"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("인증되지 않았습니다");
            }

            @Test
            void 이메일_인증이_된_경우_회원_가입을_성공_한다() {
                // given
                String email = "test@test.com";
                String password = "test";

                User user = 유저_생성(1L);
                UserAccount userAccount = 유저_계정_생성(1L, user, email, password);
                willDoNothing().given(javaMailSender).send(any(SimpleMailMessage.class));
                given(userAccountRepository.save(any(UserAccount.class))).willReturn(userAccount);
                given(userRepository.save(any(User.class))).willReturn(user);
                signUpService.sendAuthenticationTo(email);

                Cache emailAuthenticationCodeCache = (Cache) ReflectionTestUtils.getField(signUpService,
                        "emailAuthenticationCodeCache");
                Map<String, String> cache = (Map) ReflectionTestUtils.getField(emailAuthenticationCodeCache, "cache");
                String code = cache.get(email);

                assertThatThrownBy(() -> signUpService.signUp(email, "password"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("인증되지 않았습니다");

                signUpService.verifyAuthentication(email, code);

                // when, then
                signUpService.signUp(email, "password");
            }
        }
    }
}
